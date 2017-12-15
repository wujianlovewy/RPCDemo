package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;

public abstract class AbstractServer extends AbstractPeer implements Server{
	
	//绑定的地址
	private InetSocketAddress bindAddress;
	//本地地址
	private InetSocketAddress localAddress;

	AbstractServer(ChannelHandler handler, URL url) throws Exception {
		super(handler, url);
		
		String host = url.getParameter(Constants.ANYHOST_KEY, false)
                || NetUtils.isInvalidLocalHost(getUrl().getHost())
                ? NetUtils.ANYHOST : getUrl().getHost();
		bindAddress = new InetSocketAddress(host, getUrl().getPort());
		
		try{
			doOpen();
		}catch(Throwable t){
			throw new Exception(t);
		}
	}
	
	 @Override
	public void send(Object message, boolean sent) throws Throwable {
		Collection<Channel> channels = getChannels();
		for(Channel channel : channels){
			if(channel.isConnected()){
				channel.send(message, sent);
			}
		}
	}

	public InetSocketAddress getBindAddress() {
	        return bindAddress;
	 }
	 
	 protected abstract void doOpen() throws Throwable;

	@Override
	public InetSocketAddress getLocalAddress() {
		return null;
	}

	@Override
	public void close(int timeout) {
		
	}

}
