package cn.edu.wj.rpc.dubbo.remoting.api;

import java.io.IOException;
import java.nio.charset.Charset;

import cn.edu.wj.rpc.dubbo.netty.Channel;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;

public class TelnetCodec extends TransportCodec {

	//字符串序列化方式
	@Override
	public void encode(Channel channel, ChannelBuffer buffer, Object msg) throws IOException {
		if(msg instanceof String){
			byte[] msgData = ((String)msg).getBytes(getCharset(channel));
			buffer.writeBytes(msgData);
		}else{
			super.encode(channel, buffer, msg);
		}
	}
	
	@Override
	public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
    	int readable = buffer.readableBytes();
    	byte[] message = new byte[readable];
    	buffer.readBytes(message);
    	return decode(channel, buffer, readable, message);
	}
    
    protected Object decode(Channel channel, ChannelBuffer buffer, int readable, byte[] message) throws IOException{
    	checkPayload(channel, readable);
    	
    	if(message==null || message.length==0){
    		return Codec2.DecodeResult.NEED_MORE_INPUT;
    	}
    	
    	return new String(message, getCharset(channel));
    }

	private static Charset getCharset(Channel channel) {
        if (channel != null) {
            Object attribute = channel.getAttribute(Constants.CHARSET_KEY);
            if (attribute instanceof String) {
                try {
                    return Charset.forName((String) attribute);
                } catch (Throwable t) {
                   t.printStackTrace();
                }
            } else if (attribute instanceof Charset) {
                return (Charset) attribute;
            }
            URL url = channel.getUrl();
            if (url != null) {
                String parameter = url.getParameter(Constants.CHARSET_KEY);
                if (parameter != null && parameter.length() > 0) {
                    try {
                        return Charset.forName(parameter);
                    } catch (Throwable t) {
                    	 t.printStackTrace();
                    }
                }
            }
        }
        try {
            return Charset.forName("GBK");
        } catch (Throwable t) {
        	 t.printStackTrace();
        }
        return Charset.defaultCharset();
    }
	
}
