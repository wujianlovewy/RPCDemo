package cn.edu.wj.rpc.dubbo.remoting.exchange.support;

import java.util.Map;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;

public class ReplierDispatcher implements Replier<Object> {

	private final Replier<?> defaultReplier;
	
	private final Map<Class<?>, Replier<?>> repliers = new ConcurrentHashMap<Class<?>, Replier<?>>();
	
	public ReplierDispatcher(){
		this(null, null);
	}
	
	public ReplierDispatcher(Replier<?> defaultReplier){
		this(defaultReplier, null);
	}
	
	public ReplierDispatcher(Replier<?> defaultReplier, Map<Class<?>, Replier<?>> repliers){
		this.defaultReplier = defaultReplier;
		if(repliers!=null && repliers.size()>1){
			this.repliers.putAll(repliers);
		}
	}
	
	public <T> ReplierDispatcher addReplier(Class<T> type, Replier<?> replier){
		this.repliers.put(type, replier);
		return this;
	}
	
	public <T> ReplierDispatcher removeReplier(Class<T> type){
		this.repliers.remove(type);
		return this;
	}
	
	private <T> Replier<?> getReplier(Class<T> type){
		for(Map.Entry<Class<?>, Replier<?>> entryset : this.repliers.entrySet()){
			if(entryset.getKey().isAssignableFrom(type)){
				return entryset.getValue();
			}
		}
		
		if(this.defaultReplier!=null){
			return this.defaultReplier;
		}
		throw new IllegalStateException("Replier not found, Unsupported message object: " + type);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object reply(ExchangeChannel channel, Object request)
			throws Exception {
		
		return ((Replier)this.getReplier(request.getClass())).reply(channel, request);
	}

}
