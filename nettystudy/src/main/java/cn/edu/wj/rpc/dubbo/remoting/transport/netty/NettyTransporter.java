package cn.edu.wj.rpc.dubbo.remoting.transport.netty;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.netty.Client;
import cn.edu.wj.rpc.dubbo.netty.NettyClient;
import cn.edu.wj.rpc.dubbo.netty.NettyServer;
import cn.edu.wj.rpc.dubbo.netty.Server;
import cn.edu.wj.rpc.dubbo.remoting.Transporter;

public class NettyTransporter implements Transporter {

	@Override
	public Server bind(URL url, ChannelHandler handler) throws Exception {
		return new NettyServer(handler, url);
	}

	@Override
	public Client connect(URL url, ChannelHandler handler)
			throws Exception {
		return new NettyClient(handler, url);
	}

}
