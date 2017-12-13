package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;

import com.alibaba.dubbo.common.URL;

public abstract class AbstractClient extends AbstractPeer implements Client {

	AbstractClient(ChannelHandler handler, URL url) throws Throwable {
		super(handler, url);

		// 调用子类初始化
		try {
			this.doOpen();
		} catch (Throwable t) {
			close();
			throw t;
		}

		// 创建连接
		try {
			this.connect();
		} catch (Exception e) {
			throw e;
		}
	}

	protected void connect() throws Throwable {
		try {
			if (isConnected()) {
				return;
			}
			doConnect();
			if (!isConnected()) {
				throw new Exception("Fail connect to server");
			} else {
				System.out.println("Successed connect to server");
			}
		} catch (Throwable e) {
			throw e;
		}
	}
	
	@Override
	public void send(Object message, boolean sent) throws Throwable {
		Channel channel = getChannel();
		if (channel == null || !channel.isConnected()){
			throw new Exception("message can not send, because channel is closed...");
		}
		
		channel.send(message, sent);
	}

	public boolean isConnected() {
		Channel channel = getChannel();
		if (channel == null)
			return false;
		return channel.isConnected();
	}
	
	public InetSocketAddress getRemoteAddress() {
		Channel channel = this.getChannel();
		return channel.getRemoteAddress();
	}
	
	@Override
	public InetSocketAddress getLocalAddress() {
		Channel channel = this.getChannel();
		return channel.getLocalAddress();
	}

	@Override
	public void close(int timeout) {
	}

	public boolean hasAttribute(String key) {
		Channel channel = this.getChannel();
		return channel.hasAttribute(key);
	}

	public Object getAttribute(String key) {
		Channel channel = this.getChannel();
		return channel.getAttribute(key); 
	}

	public void setAttribute(String key, Object value) {
		Channel channel = this.getChannel();
		channel.setAttribute(key, value);
	}

	public void removeAttribute(String key) {
		Channel channel = this.getChannel();
		channel.removeAttribute(key);
	}

	@Override
	public void reconnect() throws Exception {
	}

	abstract Channel getChannel();

	abstract void doOpen() throws Throwable;

	abstract void doConnect() throws Throwable;
}
