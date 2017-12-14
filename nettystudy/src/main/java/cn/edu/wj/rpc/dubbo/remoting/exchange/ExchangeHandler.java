package cn.edu.wj.rpc.dubbo.remoting.exchange;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;

public interface ExchangeHandler extends ChannelHandler {

	Object reply(ExchangeChannel channel, Object request) throws Exception;
	
}
