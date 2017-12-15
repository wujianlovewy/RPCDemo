package cn.edu.wj.rpc.dubbo.remoting.exchange;

import com.alibaba.dubbo.common.URL;

public interface Exchanger {

	ExchangeServer bind(URL url, ExchangeHandler exchangeHandler) throws Exception;
	
	ExchangeClient connect(URL url, ExchangeHandler exchangeHandler) throws Exception;
}
