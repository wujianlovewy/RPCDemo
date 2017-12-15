package cn.edu.wj.rpc.dubbo.remoting.transport;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;

public class ChannelHandlerAdapter implements ChannelHandler {

	@Override
	public void connected(Channel channel) throws Exception {

	}

	@Override
	public void disconnected(Channel channel) throws Exception {

	}

	@Override
	public void sent(Channel channel, Object message) throws Exception {

	}

	@Override
	public void received(Channel channel, Object message) throws Exception {

	}

	@Override
	public void caught(Channel channel, Throwable exception) throws Exception {

	}

}
