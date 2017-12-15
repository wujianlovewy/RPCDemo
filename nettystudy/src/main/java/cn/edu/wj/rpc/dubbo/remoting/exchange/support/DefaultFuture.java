package cn.edu.wj.rpc.dubbo.remoting.exchange.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.remoting.TimeoutException;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Request;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Response;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseCallback;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseFuture;

public class DefaultFuture implements ResponseFuture {

	private static final Logger logger = Logger.getLogger(DefaultFuture.class);

	private static final Map<Long, Channel> CHANNELS = new ConcurrentHashMap<Long, Channel>();

	private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();

	private final Lock lock = new ReentrantLock();
	private final Condition done = lock.newCondition();

	private volatile Response response;

	private volatile long sent;

	private final Channel channel;
	private final Request request;
	private final int timeout;
	private final long id;
	
	private final long start = System.currentTimeMillis();

	public DefaultFuture(Channel channel, Request request, int timeout) {
		this.channel = channel;
		this.request = request;
		this.id = request.getId();
		this.timeout = timeout > 0 ? timeout : channel.getUrl()
				.getPositiveParameter(Constants.TIMEOUT_KEY,
						Constants.DEFAULT_TIMEOUT);

		FUTURES.put(id, this);
		CHANNELS.put(id, channel);
	}

	@Override
	public Object get() throws Exception {
		return this.get(timeout);
	}

	@Override
	public Object get(int timeout) throws Exception {
		if (timeout <= 0) {
			timeout = Constants.DEFAULT_TIMEOUT;
		}
		
		if(!isDone()){
			long start = System.currentTimeMillis();
			lock.lock();
			try{
				while(!isDone()){
					done.await(timeout, TimeUnit.MILLISECONDS);
					if(isDone() || System.currentTimeMillis() - start > this.timeout){
						break;
					}
				}
			}finally{
				lock.unlock();
			}
		
			if(!isDone()){
				throw new RuntimeException(getTimeoutMessage(false));
			}
		}
		return returnFromResponse();
	}
	
	public void cancel() {
		Response errorResult = new Response(this.id);
		errorResult.setErrorMessage("request future has been canceled.");
        response = errorResult;
        
        FUTURES.remove(this.id);
        CHANNELS.remove(this.id);
	}
	
	private Object returnFromResponse() throws Exception{
		Response resp = this.response;
		if(resp==null){
			throw new IllegalArgumentException("response cannot be null");
		}
		if(resp.getStatus() == Response.OK){
			return resp.getResult();
		}
		
		if(resp.getStatus() == Response.CLIENT_TIMEOUT || resp.getStatus() == Response.SERVER_TIMEOUT){
			throw new TimeoutException(resp.getStatus() == Response.SERVER_TIMEOUT, null, resp.getErrorMessage());
		}
		
		throw new Exception(resp.getErrorMessage());
	}

	public static void sent(Channel channel, Request request) {
		DefaultFuture future = FUTURES.get(request.getId());
		if (future != null) {
			future.doSent();
		}
	}
	
	 public boolean isDone() {
		 return this.response!=null;
	 }

	public static void received(Channel channel, Response response) {
		try {
			DefaultFuture future = FUTURES.remove(response.getId());
			if (future != null) {
				future.doReceived(response);
			} else {
				logger.warn("The timeout response finally returned at "
						+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
								.format(new Date()))
						+ ", response "
						+ response
						+ (channel == null ? "" : ", channel: "
								+ channel.getLocalAddress() + " -> "
								+ channel.getRemoteAddress()));
			}
		} finally {
			CHANNELS.remove(response.getId());
		}
	}

	private void doSent() {
		sent = System.currentTimeMillis();
	}

	private void doReceived(Response response) {
		lock.lock();
		try {
			this.response = response;
			if (done != null) {
				this.done.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void setCallback(ResponseCallback callback) {

	}

	private String getTimeoutMessage(boolean scan) {
        long nowTimestamp = System.currentTimeMillis();
        return (sent > 0 ? "Waiting server-side response timeout" : "Sending request timeout in client-side")
                + (scan ? " by scan timer" : "") + ". start time: "
                + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(start))) + ", end time: "
                + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())) + ","
                + (sent > 0 ? " client elapsed: " + (sent - start)
                + " ms, server elapsed: " + (nowTimestamp - sent)
                : " elapsed: " + (nowTimestamp - start)) + " ms, timeout: "
                + timeout + " ms, request: " + request + ", channel: " + channel.getLocalAddress()
                + " -> " + channel.getRemoteAddress();
	}

}
