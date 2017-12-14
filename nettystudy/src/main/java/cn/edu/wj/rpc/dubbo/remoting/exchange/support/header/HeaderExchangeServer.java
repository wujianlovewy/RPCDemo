package cn.edu.wj.rpc.dubbo.remoting.exchange.support.header;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.dubbo.remoting.exchange.support.header.HeaderExchangeChannel;
import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.netty.Server;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeServer;

public class HeaderExchangeServer implements ExchangeServer {
	
	private final Server server;
	
	//server是否关闭标识
	private AtomicBoolean closed = new AtomicBoolean(false);
	
	public HeaderExchangeServer(Server server){
		if(server==null){
			throw new IllegalArgumentException("server == null");
		}
		this.server = server;
	}

	@Override
	public Collection<ExchangeChannel> getExchangeChannels() {
		Collection<ExchangeChannel> exchangeChannels = new ArrayList<ExchangeChannel>();
		Collection<Channel> channels = server.getChannels();
		
		if(channels!=null && channels.size()>0){
			for(Channel channel : channels){
				exchangeChannels.add(HeaderExchangeChannel.getOrAddExchangeChannel(channel));
			}
		}
		
		return exchangeChannels;
	}

	@Override
	public ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress) {
		Channel channel = server.getChannel(remoteAddress);
		return HeaderExchangeChannel.getOrAddExchangeChannel(channel);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<Channel> getChannels() {
		return (Collection)this.getExchangeChannels();
	}

	@Override
	public Channel getChannel(InetSocketAddress remoteAddress) {
		return this.getExchangeChannel(remoteAddress);
	}

	@Override
	public boolean isBound() {
		return this.server.isBound();
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return this.server.getLocalAddress();
	}

	@Override
	public void send(Object message) throws Throwable {
		if(this.closed.get()){
			throw new Exception(this.getLocalAddress()+"Failed to send message " + message + ", cause: The server " + getLocalAddress() + " is closed!");
		}
		this.server.send(message);
	}

	@Override
	public void send(Object message, boolean sent) throws Throwable {
		if(this.closed.get()){
			throw new Exception(this.getLocalAddress()+"Failed to send message " + message + ", cause: The server " + getLocalAddress() + " is closed!");
		}
		this.server.send(message, sent);
	}

	@Override
	public boolean isClosed() {
		return this.server.isClosed();
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public void close() {
		
	}

	@Override
	public void close(int timeout) {
		
	}

	@Override
	public ChannelHandler getChannelHandler() {
		return server.getChannelHandler();
	}

}
