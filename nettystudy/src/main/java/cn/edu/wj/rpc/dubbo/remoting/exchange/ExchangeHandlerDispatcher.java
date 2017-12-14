package cn.edu.wj.rpc.dubbo.remoting.exchange;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.Replier;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.ReplierDispatcher;
import cn.edu.wj.rpc.dubbo.remoting.transport.ChannelHandlerDispatcher;

public class ExchangeHandlerDispatcher implements ExchangeHandler {
	
	private final ChannelHandlerDispatcher handlerDispatcher;
	private final ReplierDispatcher replierDispatcher;
	
	public ExchangeHandlerDispatcher(){
		this.handlerDispatcher = new ChannelHandlerDispatcher();
		this.replierDispatcher = new ReplierDispatcher();
	}
	
	public ExchangeHandlerDispatcher(Replier<?> replier){
		this.handlerDispatcher = new ChannelHandlerDispatcher();
		this.replierDispatcher = new ReplierDispatcher(replier);
	}
	
	public ExchangeHandlerDispatcher(ChannelHandler... handlers){
		this.handlerDispatcher = new ChannelHandlerDispatcher(handlers);
		this.replierDispatcher = new ReplierDispatcher();
	}
	
	public ExchangeHandlerDispatcher(Replier<?> replier, ChannelHandler... handlers){
		this.handlerDispatcher = new ChannelHandlerDispatcher(handlers);
		this.replierDispatcher = new ReplierDispatcher(replier);
	}
	
	@Override
	public Object reply(ExchangeChannel channel, Object request)
			throws Exception {
		return this.replierDispatcher.reply(channel, request); 
	}

	@Override
	public void connected(Channel channel) throws Exception {
		this.handlerDispatcher.connected(channel);
	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		this.handlerDispatcher.disconnected(channel);
	}

	@Override
	public void sent(Channel channel, Object message) throws Exception {
		this.handlerDispatcher.sent(channel, message);
	}

	@Override
	public void received(Channel channel, Object message) throws Exception {
		this.handlerDispatcher.received(channel, message);
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws Exception {
		this.handlerDispatcher.caught(channel, exception);
	}

}
