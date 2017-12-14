package cn.edu.wj.rpc.dubbo.remoting.transport;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.netty.Server;
import cn.edu.wj.rpc.dubbo.remoting.Transporter;

import com.alibaba.dubbo.common.URL;

public class Transporters {

	public static Server bind(URL url, ChannelHandler... handlers) throws Exception {
		if (url == null) { 
			throw new IllegalArgumentException("url == null");
		}
		if (handlers == null || handlers.length == 0) {
			throw new IllegalArgumentException("handlers == null");
		}
		
		ChannelHandler channelHandler = null;
		if(handlers.length==1){
			channelHandler = handlers[0];
		}else{
			channelHandler = new ChannelHandlerDispatcher(handlers);
		}
		
		return getTransporter().bind(url, channelHandler);
	}
	
	public static Transporter getTransporter(){
		return null;
	}
	
	

}
