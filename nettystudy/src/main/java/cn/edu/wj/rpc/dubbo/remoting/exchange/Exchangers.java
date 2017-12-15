package cn.edu.wj.rpc.dubbo.remoting.exchange;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.Replier;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.header.HeaderExchanger;
import cn.edu.wj.rpc.dubbo.remoting.transport.ChannelHandlerAdapter;

public class Exchangers {

	private Exchangers(){}
	
	public static ExchangeServer bind(String url, Replier<?> replier) throws Exception{
		return bind(URL.valueOf(url), replier);
	}
	
	public static ExchangeServer bind(String url,ChannelHandler channelHandler, Replier<?> replier) throws Exception{
		return bind(URL.valueOf(url), replier);
	}
	
	public static ExchangeServer bind(URL url, Replier<?> replier) throws Exception{
		return bind(url, new ChannelHandlerAdapter(), replier);
	}
	
	public static ExchangeServer bind(URL url,ChannelHandler channelHandler, Replier<?> replier) throws Exception{
		return bind(url, new ExchangeHandlerDispatcher(replier, channelHandler));
	}
	
	public static ExchangeServer bind(URL url, ExchangeHandler exchangeHandler) throws Exception{
		return getExchanger(url).bind(url, exchangeHandler);
	}
	

	public static ExchangeClient connect(URL url) throws Exception{
		return connect(url, new ChannelHandlerAdapter(), null);
	}
	
	public static ExchangeClient connect(String url, Replier<?> replier) throws Exception{
		return connect(URL.valueOf(url), replier);
	}
	
	public static ExchangeClient connect(String url,ChannelHandler channelHandler, Replier<?> replier) throws Exception{
		return connect(URL.valueOf(url), replier);
	}
	
	public static ExchangeClient connect(URL url, Replier<?> replier) throws Exception{
		return connect(url, new ChannelHandlerAdapter(), replier);
	}
	
	public static ExchangeClient connect(URL url,ChannelHandler channelHandler, Replier<?> replier) throws Exception{
		return connect(url, new ExchangeHandlerDispatcher(replier, channelHandler));
	}
	
	public static ExchangeClient connect(URL url, ExchangeHandler exchangeHandler) throws Exception{
		return getExchanger(url).connect(url, exchangeHandler);
	}
	
	public static Exchanger getExchanger(URL url){
		return new HeaderExchanger();
	}
	
}
