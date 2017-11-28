package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;

public abstract class AbstractServer extends AbstractPeer {
	
	//绑定的地址
	private InetSocketAddress bindAddress;
	//本地地址
	private InetSocketAddress localAddress;

	AbstractServer(ChannelHandler handler, URL url) throws Throwable {
		super(handler, url);
		
		String host = url.getParameter(Constants.ANYHOST_KEY, false)
                || NetUtils.isInvalidLocalHost(getUrl().getHost())
                ? NetUtils.ANYHOST : getUrl().getHost();
		bindAddress = new InetSocketAddress(host, getUrl().getPort());
		
		try{
			doOpen();
		}catch(Throwable t){
			throw t;
		}
	}
	
	 @Override
	public void send(Object message, boolean sent) throws Throwable {
		
	}

	public InetSocketAddress getBindAddress() {
	        return bindAddress;
	 }
	 
	 protected abstract void doOpen() throws Throwable;

}
