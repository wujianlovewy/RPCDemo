package cn.edu.wj.dubbo.remoting.exchange.support.header;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Request;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Response;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseFuture;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

public final class HeaderExchangeChannel implements ExchangeChannel {
	
	private static final Logger logger = Logger.getLogger(HeaderExchangeChannel.class);
	
	 private static final String CHANNEL_KEY = HeaderExchangeChannel.class.getName() + ".CHANNEL";
	 
	 private final Channel channel;
	 
	 private volatile boolean closed = false;
	 
	 HeaderExchangeChannel(Channel channel){
		 if(channel == null){
			 throw new IllegalArgumentException("channel == null");
		 }
		 this.channel = channel;
	 }
	 
	 public static HeaderExchangeChannel getOrAddExchangeChannel(Channel ch){
		 if(ch == null) return null;
		 HeaderExchangeChannel hec = (HeaderExchangeChannel)ch.getAttribute(CHANNEL_KEY);
		 if(hec==null){
			 hec = new HeaderExchangeChannel(ch);
			 if(ch.isConnected()){
				 ch.setAttribute(CHANNEL_KEY, hec);
			 }
		 }
		 return hec;
	 }
	 
	 public static void removeChannelIfDisconnected(Channel ch){
		 if(ch!=null && !ch.isConnected()){
			 ch.removeAttribute(CHANNEL_KEY);
		 }
	 }

	@Override
	public InetSocketAddress getRemoteAddress() {
		return channel.getRemoteAddress();
	}

	@Override
	public boolean isConnected() {
		return channel.isConnected();
	}

	@Override
	public boolean hasAttribute(String key) {
		return channel.hasAttribute(key);
	}

	@Override
	public Object getAttribute(String key) {
		return channel.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		channel.setAttribute(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		channel.removeAttribute(key);
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return channel.getLocalAddress();
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	public URL getUrl() {
		return channel.getUrl();
	}

	@Override
	public void close() {
		try {
			channel.close();
		}catch(Throwable t){
			logger.error(t.getMessage(), t);
		}
	}
	
	@Override
	public void close(int timeout) {

	}
	
	@Override
	public void send(Object message) throws Throwable {
		this.send(message, this.getUrl().getParameter(Constants.SENT_KEY, false));
	}

	@Override
	public void send(Object message, boolean sent) throws Throwable {
		if(closed){
			throw new Exception(this.getRemoteAddress() + "Failed to send message " + message + ", cause: The channel " + this + " is closed!");
		}
		
		if(message instanceof Request
				|| message instanceof Response
				|| message instanceof String){
			this.channel.send(message, sent);
		}else{
			Request request = new Request();
			request.setTwoWay(false);
			request.setVersion("2.0.0");
			request.setData(message);
			this.channel.send(request);
		}
	}

	@Override
	public ResponseFuture request(Object request) throws Exception {
		return this.request(request, getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT)); 
	}

	@Override
	public ResponseFuture request(Object request, int timeout) throws Exception {
		return null;
	}

	@Override
	public ExchangeHandler getExchangeHandler() {
		return (ExchangeHandler)channel.getChannelHandler();
	}


	@Override
	public ChannelHandler getChannelHandler() {
		return channel.getChannelHandler();
	}



}
