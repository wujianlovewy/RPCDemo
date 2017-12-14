package cn.edu.wj.rpc.dubbo.remoting.exchange.support;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseCallback;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseFuture;

public class DefaultFuture implements ResponseFuture {
	
	private static final Logger logger = Logger.getLogger(DefaultFuture.class);
	
	private static final Map<Long, Channel> CHANNELS = new ConcurrentHashMap<Long, Channel>();
	
	private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();

	@Override
	public Object get() throws Exception {
		return null;
	}

	@Override
	public Object get(int timeoutInMillis) throws Exception {
		return null;
	}

	@Override
	public void setCallback(ResponseCallback callback) {

	}

	@Override
	public boolean isDone() {
		return false;
	}

}
