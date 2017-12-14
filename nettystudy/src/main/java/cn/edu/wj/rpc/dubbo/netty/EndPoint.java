package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;

import com.alibaba.dubbo.common.URL;

public interface EndPoint {

	InetSocketAddress getLocalAddress();
	
	void send(Object message) throws Throwable;
	
	//sent 是否等待发送完成
	void send(Object message, boolean sent) throws Throwable;
	
	boolean isClosed();
	
	public URL getUrl();
	

    /**
     * close the channel.
     */
    void close();

    /**
     * Graceful close the channel.
     */
    void close(int timeout);
    
    ChannelHandler getChannelHandler();
	
}
