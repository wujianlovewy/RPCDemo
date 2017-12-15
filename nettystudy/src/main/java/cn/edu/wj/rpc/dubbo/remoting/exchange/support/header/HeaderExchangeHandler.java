package cn.edu.wj.rpc.dubbo.remoting.exchange.support.header;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Request;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Response;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.DefaultFuture;
import cn.edu.wj.rpc.dubbo.remoting.transport.ChannelHandlerDelegate;

public class HeaderExchangeHandler implements ChannelHandlerDelegate {
	
	private final ExchangeHandler handler;
	
	public HeaderExchangeHandler(ExchangeHandler handler) {
		if(handler == null){
			throw new IllegalArgumentException("exchangeHandler == null");
		}
		this.handler = handler;
	}
	
	@Override
	public void sent(Channel channel, Object message) throws Exception {
		Throwable exception = null;
		try {
			ExchangeChannel exchangeChannel = HeaderExchangeChannel.getOrAddExchangeChannel(channel);
			try{
				this.handler.sent(exchangeChannel, message);
			}finally{
				HeaderExchangeChannel.removeChannelIfDisconnected(channel);
			}
		} catch (Exception e) {
			exception = e;
		}
		
		if(message instanceof Request){
			Request request = (Request)message;
			DefaultFuture.sent(channel, request);
		}
		
		if(exception!=null){
			throw (Exception)exception;
		}
	}

	@Override
	public void received(Channel channel, Object message) throws Exception {
		ExchangeChannel exchangeChannel = HeaderExchangeChannel.getOrAddExchangeChannel(channel);
		try{
			if(message instanceof Request){
				Request request = (Request) message;
				 if (request.isTwoWay()) {
					 Response resp = this.handleRequest(exchangeChannel, request);
					 channel.send(resp);
				 }else{
					 this.handler.received(exchangeChannel, request.getData());
				 }
			}else if(message instanceof Response){
				handleResponse(channel, (Response) message);
			}else if(message instanceof String){
				
			}else{
				this.handler.received(exchangeChannel, message);
			}
		}catch(Throwable t){
			throw new Exception(t);
		}finally{
			HeaderExchangeChannel.removeChannelIfDisconnected(channel);
		}
	}
	
	Response handleRequest(ExchangeChannel channel, Request request){
		Response res = new Response(request.getId(), request.getVersion());
		// find handler by message class.
        Object msg = request.getData();
        try {
            // handle data.
            Object result = handler.reply(channel, msg);
            res.setStatus(Response.OK);
            res.setResult(result);
        } catch (Throwable e) {
            res.setStatus(Response.SERVICE_ERROR);
            res.setErrorMessage(StringUtils.toString(e));
        }
		return res;
	}
	
	static void handleResponse(Channel channel, Response response){
		 if (response != null && !response.isHeartbeat()) {
			 DefaultFuture.received(channel, response);
		 }
	}
	
	@Override
	public void connected(Channel channel) throws Exception {
		ExchangeChannel exchangeChannel = HeaderExchangeChannel.getOrAddExchangeChannel(channel);
		try{
			this.handler.connected(exchangeChannel);
		}finally{
			HeaderExchangeChannel.removeChannelIfDisconnected(channel);
		}
	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		ExchangeChannel exchangeChannel = HeaderExchangeChannel.getOrAddExchangeChannel(channel);
		try{
			this.handler.disconnected(exchangeChannel);
		}finally{
			HeaderExchangeChannel.removeChannelIfDisconnected(channel);
		}
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws Exception {
		ExchangeChannel exchangeChannel = HeaderExchangeChannel.getOrAddExchangeChannel(channel);
		try{
			this.handler.caught(exchangeChannel, exception);
		}finally{
			HeaderExchangeChannel.removeChannelIfDisconnected(channel);
		}
	}

	@Override
	public ChannelHandler getHandler() {
		return this.handler;
	}

}
