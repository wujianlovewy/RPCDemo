package cn.edu.wj.rpc.dubbo.remoting.exchange.support.header;

import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeHandler;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeServer;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Exchanger;
import cn.edu.wj.rpc.dubbo.remoting.transport.Transporters;

import com.alibaba.dubbo.common.URL;

public class HeaderExchanger implements Exchanger {

	public static final String NAME = "header";
	
	@Override
	public ExchangeServer bind(URL url, ExchangeHandler handler)
			throws Exception {
		return new HeaderExchangeServer(Transporters.bind(url, new HeaderExchangeHandler(handler)));  
	}

}
