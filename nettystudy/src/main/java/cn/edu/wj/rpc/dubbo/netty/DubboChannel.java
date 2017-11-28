package cn.edu.wj.rpc.dubbo.netty;

import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

public class DubboChannel extends AbstractPeer implements Channel{

	 private final org.jboss.netty.channel.Channel channel;
	 
	 private ChannelHandler handler;
	 
	 //映射netty channel与dubbo channel的关系
	 private static final ConcurrentMap<org.jboss.netty.channel.Channel, DubboChannel> channelMap = new ConcurrentHashMap<>();
	 
	 DubboChannel(org.jboss.netty.channel.Channel channel, ChannelHandler handler, URL url){
		 super(handler, url);
		 this.channel = channel;
	 }
	 
	 //绑定netty与dubbo的channel
	 static DubboChannel getOrAddChannel(org.jboss.netty.channel.Channel channel, ChannelHandler handler, URL url){
		 if(null==channel){
			 return null;
		 }
		 DubboChannel dubboChannel = channelMap.get(channel);
		 if(null==dubboChannel){
			 DubboChannel dc = new DubboChannel(channel, handler, url);
			 if(channel.isConnected()){
				 dubboChannel = channelMap.putIfAbsent(channel, dc);
			 }
			 if(dubboChannel==null){
				 dubboChannel = dc;
			 }
		 }
		 return dubboChannel;
	 }
	 
	 //删除连接
	 static void removeChannelIfDisconnected(org.jboss.netty.channel.Channel ch) {
		 if(ch!=null && !ch.isConnected()){
			 channelMap.remove(ch);
		 }
	 }

	@Override
	public boolean isConnected() {
		return this.channel.isConnected();
	}

	@Override
	public void send(Object message, boolean sent) throws Throwable {
		if(isClosed()){
			throw new Throwable("Failed to send message cause Channel closed");
		}
		int timeout = 0;
		boolean success = true;
		
		try{
			ChannelFuture future = this.channel.write(message);
			 if (sent) {
	             timeout = getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, 2000);
	             success = future.await(timeout);
	         }
	         Throwable cause = future.getCause();
	         if (cause != null) {
	             throw cause;
	         }
	         
	         if(!success){
	        	 throw new Throwable("Failed to send message");
	         }
		}catch(Throwable t){
			throw t;
		}
		
	}
}
