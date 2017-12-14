package cn.edu.wj.rpc.dubbo.remoting.exchange.support;

import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;

public interface Replier<T> {

	Object reply(ExchangeChannel channel, T request) throws Exception;
	
}
