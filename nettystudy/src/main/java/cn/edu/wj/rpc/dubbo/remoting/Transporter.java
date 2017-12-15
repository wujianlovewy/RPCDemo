package cn.edu.wj.rpc.dubbo.remoting;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.netty.Client;
import cn.edu.wj.rpc.dubbo.netty.Server;

public interface Transporter {

	Server bind(URL url, ChannelHandler channelHandler) throws Exception;
	
	Client connect(URL url, ChannelHandler channelHandler) throws Exception;
}
