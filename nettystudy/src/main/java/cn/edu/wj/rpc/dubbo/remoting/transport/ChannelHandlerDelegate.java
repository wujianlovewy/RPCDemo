package cn.edu.wj.rpc.dubbo.remoting.transport;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;

public interface ChannelHandlerDelegate extends ChannelHandler {

		ChannelHandler getHandler();
	
}
