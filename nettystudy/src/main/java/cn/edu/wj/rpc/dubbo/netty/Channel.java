package cn.edu.wj.rpc.dubbo.netty;

import com.alibaba.dubbo.common.URL;

public interface Channel extends EndPoint {

	boolean isConnected();
	
	public URL getUrl();
	
}
