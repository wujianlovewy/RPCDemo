package cn.edu.wj.rpc.dubbo.remoting.exchange;

import cn.edu.wj.rpc.dubbo.netty.Channel;

public interface ExchangeChannel extends Channel {

	ResponseFuture request(Object request) throws Exception;
	
	ResponseFuture request(Object request, int timeout) throws Exception;
	
	ExchangeHandler getExchangeHandler();
	
	void close(int timeout);
	
}
