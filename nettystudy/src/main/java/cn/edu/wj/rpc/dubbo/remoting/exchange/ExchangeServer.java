package cn.edu.wj.rpc.dubbo.remoting.exchange;

import java.net.InetSocketAddress;
import java.util.Collection;

import cn.edu.wj.rpc.dubbo.netty.Server;

public interface ExchangeServer extends Server {

	Collection<ExchangeChannel> getExchangeChannels();
	
	ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress);
}
