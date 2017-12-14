package cn.edu.wj.dubbo.remoting.exchange.support.header;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;

public class MockChannel implements Channel {

	private Map<String, Object> attributes = new HashMap<String, Object>();

    private volatile boolean closed = false;
    private volatile boolean closing = false;
    private List<Object> sentObjects = new ArrayList<Object>();
	
	@Override
	public InetSocketAddress getLocalAddress() {
		return null;
	}

	@Override
	public void send(Object message) throws Throwable {
		sentObjects.add(message);
	}

	@Override
	public void send(Object message, boolean sent) throws Throwable {
		sentObjects.add(message);
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public void close() {
		this.closed = true;
	}

	@Override
	public void close(int timeout) {
		this.closed = true;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return null;
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	public List<Object> getSentObjects() {
		return Collections.unmodifiableList(this.sentObjects);
	}

	@Override
	public ChannelHandler getChannelHandler() {
		return null;
	}
	
}
