package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface Server extends EndPoint{

	Collection<Channel> getChannels();
	
	Channel getChannel(InetSocketAddress remoteAddress);
	
	boolean isBound();
	
}
