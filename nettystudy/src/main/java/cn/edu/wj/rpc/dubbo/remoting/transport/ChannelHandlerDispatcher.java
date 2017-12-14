package cn.edu.wj.rpc.dubbo.remoting.transport;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;

public class ChannelHandlerDispatcher implements ChannelHandler{
	
	private static final Logger logger = Logger.getLogger(ChannelHandlerDispatcher.class);
	
	private final Collection<ChannelHandler> channelHandlers = new CopyOnWriteArraySet<ChannelHandler>();

	public ChannelHandlerDispatcher(ChannelHandler... handlers){
		this(handlers==null ? null : Arrays.asList(handlers));
	}
	
	public ChannelHandlerDispatcher(Collection<ChannelHandler> handlers){
		if(handlers!=null && handlers.size()>0){
			this.channelHandlers.addAll(handlers);
		}
	}
	
	@Override
	public void connected(Channel channel) throws Exception {
		for(ChannelHandler listener : channelHandlers){
			 try {
				 listener.connected(channel);
			 }catch(Throwable t){
				 logger.error(t.getMessage(), t);
			 }
		}
	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		for(ChannelHandler listener : channelHandlers){
			 try {
				 listener.disconnected(channel);
			 }catch(Throwable t){
				 logger.error(t.getMessage(), t);
			 }
		}
	}

	@Override
	public void sent(Channel channel, Object message) throws Exception {
		for(ChannelHandler listener : channelHandlers){
			 try {
				 listener.sent(channel, message);
			 }catch(Throwable t){
				 logger.error(t.getMessage(), t);
			 }
		}
	}

	@Override
	public void received(Channel channel, Object message) throws Exception {
		for(ChannelHandler listener : channelHandlers){
			 try {
				 listener.received(channel, message);
			 }catch(Throwable t){
				 logger.error(t.getMessage(), t);
			 }
		}
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws Exception {
		for(ChannelHandler listener : channelHandlers){
			 try {
				 listener.caught(channel, exception);
			 }catch(Throwable t){
				 logger.error(t.getMessage(), t);
			 }
		}
	}

}
