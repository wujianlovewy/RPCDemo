package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;
import java.util.Map;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;

public class NettyHandler extends SimpleChannelHandler {

	private final ChannelHandler handler;
	
	//<ip:port, channel> 根据IP:PORT存放DUBBO channel
	private Map<String, Channel> channels = new ConcurrentHashMap<String, Channel>();
	
	private URL url;
	
	public NettyHandler(ChannelHandler channelHandler, URL url){
		this.handler = channelHandler;
		this.url = url;
	}
	
	public Map<String, Channel> getChannels(){
		return channels;
	}
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public ChannelHandler getHandler() {
		return handler;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		DubboChannel channel = DubboChannel.getOrAddChannel(ctx.getChannel(), this.handler, this.getUrl());
		try{
			if(null!=channel){
				this.channels.put(NetUtils.toAddressString((InetSocketAddress)ctx.getChannel().getRemoteAddress()), channel);
			}
			
			//调用自定义channelhandler处理
			this.handler.connected(channel);
		}finally{
			DubboChannel.removeChannelIfDisconnected(ctx.getChannel());
		}
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		DubboChannel channel = DubboChannel.getOrAddChannel(ctx.getChannel(), this.handler, this.getUrl());
		try{
			this.channels.remove(NetUtils.toAddressString((InetSocketAddress)ctx.getChannel().getRemoteAddress()));
			this.handler.disconnected(channel);
		}finally{
			DubboChannel.removeChannelIfDisconnected(ctx.getChannel());
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		//根据netty转换成dubbo的channel
		DubboChannel channel = DubboChannel.getOrAddChannel(ctx.getChannel(), this.handler, this.getUrl());
		try{
			this.handler.received(channel, e.getMessage());
		}finally{
			DubboChannel.removeChannelIfDisconnected(ctx.getChannel());
		}
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		DubboChannel channel = DubboChannel.getOrAddChannel(ctx.getChannel(), this.handler, this.getUrl());
		try{
			this.handler.sent(channel, e.getMessage());
		}finally{
			DubboChannel.removeChannelIfDisconnected(ctx.getChannel());
		}
	}
	
}
