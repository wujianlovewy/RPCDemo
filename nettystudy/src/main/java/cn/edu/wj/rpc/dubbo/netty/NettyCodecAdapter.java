package cn.edu.wj.rpc.dubbo.netty;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.buffer.DynamicChannelBuffer;

import cn.edu.wj.rpc.dubbo.remoting.api.Codec2;

/**
 * @author jwu
 * 自定义netty编码、解码适配器
 * netty需要研究下
 */
public final class NettyCodecAdapter {

	private final Codec2 codec;
	
	private final URL url;
	private final ChannelHandler handler;
	
	private final int bufferSize;
	
	private final InternalEncoder encoder = new InternalEncoder();
	private final InternalDecoder decoder = new InternalDecoder();
	
	public NettyCodecAdapter(Codec2 codec, URL url, ChannelHandler handler){
		this.codec =codec;
		this.url =url;
		this.handler = handler;
		this.bufferSize = Constants.DEFAULT_BUFFER_SIZE;
	}
	
	@Sharable
	private class InternalEncoder extends OneToOneEncoder{
		@Override
		protected Object encode(ChannelHandlerContext ctx, Channel channel,
				Object msg) throws Exception {
			com.alibaba.dubbo.remoting.buffer.ChannelBuffer buffer =
                    com.alibaba.dubbo.remoting.buffer.ChannelBuffers.dynamicBuffer(1024);
			DubboChannel dubboChannel = DubboChannel.getOrAddChannel(channel, handler, url);
			try {
				codec.encode(dubboChannel, buffer, msg);
			}finally{
				DubboChannel.removeChannelIfDisconnected(channel);
			}
			return ChannelBuffers.wrappedBuffer(buffer.toByteBuffer()); // bug--buffer.array()导致发序列化异常
		}
	}
	
	private class InternalDecoder extends SimpleChannelUpstreamHandler{

		 private com.alibaba.dubbo.remoting.buffer.ChannelBuffer buffer =
	                com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
				throws Exception {
			 Object o = event.getMessage();
            if (!(o instanceof ChannelBuffer)) {
                ctx.sendUpstream(event);
                return;
            }
            
            ChannelBuffer input = (ChannelBuffer) o;
            int readable = input.readableBytes();
            if (readable <= 0) {
                return;
            }
            
            com.alibaba.dubbo.remoting.buffer.ChannelBuffer message;
            if (buffer.readable()) {
                if (buffer instanceof DynamicChannelBuffer) {
                    buffer.writeBytes(input.toByteBuffer());
                    message = buffer;
                } else {
                    int size = buffer.readableBytes() + input.readableBytes();
                    message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.dynamicBuffer(
                            size > bufferSize ? size : bufferSize);
                    message.writeBytes(buffer, buffer.readableBytes());
                    message.writeBytes(input.toByteBuffer());
                }
            } else {
                message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.wrappedBuffer(
                        input.toByteBuffer());
            }
            
            DubboChannel dubboChannel = DubboChannel.getOrAddChannel(ctx.getChannel(), handler, url);
            Object msg;
            int saveReaderIndex;
            
            try {
                // decode object.
                do {
                    saveReaderIndex = message.readerIndex();
                    try {
                        msg = codec.decode(dubboChannel, message);
                    } catch (IOException e) {
                        buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                        throw e;
                    }
                    if (msg == Codec2.DecodeResult.NEED_MORE_INPUT) {
                        message.readerIndex(saveReaderIndex);
                        break;
                    } else {
                        if (saveReaderIndex == message.readerIndex()) {
                            buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                            throw new IOException("Decode without read data.");
                        }
                        if (msg != null) {
                            Channels.fireMessageReceived(ctx, msg, event.getRemoteAddress());
                        }
                    }
                } while (message.readable());
            } finally {
                if (message.readable()) {
                    message.discardReadBytes();
                    buffer = message;
                } else {
                    buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                }
                DubboChannel.removeChannelIfDisconnected(ctx.getChannel());
            }
		}
		
	}

	public InternalEncoder getEncoder() {
		return encoder;
	}

	public InternalDecoder getDecoder() {
		return decoder;
	}
	
}
