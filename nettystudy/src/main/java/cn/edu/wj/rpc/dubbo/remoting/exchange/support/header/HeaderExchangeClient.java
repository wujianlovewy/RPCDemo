package cn.edu.wj.rpc.dubbo.remoting.exchange.support.header;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.netty.Client;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeClient;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseFuture;

import com.alibaba.dubbo.common.URL;

public class HeaderExchangeClient implements ExchangeClient {

	private static final Logger logger = org.apache.log4j.Logger.getLogger(HeaderExchangeClient.class);
	
	private final Client client;
	private final ExchangeChannel channel;
	
	HeaderExchangeClient(Client client, boolean needHeartbeat){
		this.client = client;
		this.channel = new HeaderExchangeChannel(client);
	}

	@Override
	public void reconnect() throws Exception {
	}

	@Override
	public ResponseFuture request(Object request) throws Exception {
		return channel.request(request);
	}

	@Override
	public ResponseFuture request(Object request, int timeout) throws Exception {
		return channel.request(request, timeout);
	}

	@Override
	public ExchangeHandler getExchangeHandler() {
		return channel.getExchangeHandler();
	}

	@Override
	public void close(int timeout) {
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAttribute(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public InetSocketAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(Object message) throws Throwable {
		channel.send(message);
	}

	@Override
	public void send(Object message, boolean sent) throws Throwable {
		channel.send(message, sent);
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URL getUrl() {
		return channel.getUrl();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public ChannelHandler getChannelHandler() {
		return channel.getChannelHandler();
	}

}
