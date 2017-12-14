package cn.edu.wj.rpc.dubbo.remoting.exchange.support.header;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeHandler;
import cn.edu.wj.rpc.dubbo.remoting.transport.ChannelHandlerDelegate;

public class HeaderExchangeHandler implements ChannelHandlerDelegate {
	public HeaderExchangeHandler(ExchangeHandler handler) {

	}

	@Override
	public void connected(Channel channel) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sent(Channel channel, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void received(Channel channel, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void caught(Channel channel, Throwable exception) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ChannelHandler getHandler() {
		// TODO Auto-generated method stub
		return null;
	}

}
