package cn.edu.wj.rpc.dubbo.netty;

import java.net.InetSocketAddress;

import cn.edu.wj.rpc.dubbo.common.support.FastJsonSerialization;
import cn.edu.wj.rpc.dubbo.remoting.api.Codec2;
import cn.edu.wj.rpc.dubbo.remoting.api.TransportCodec;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;

public abstract class AbstractPeer implements ChannelHandler,EndPoint{

	private final ChannelHandler handler;
	
	private volatile URL url;
	
	// closing closed分别表示关闭流程中、完成关闭
    private volatile boolean closing;
    private volatile boolean closed;
    
    //序列化方式
    private Codec2 codec;
	
	AbstractPeer(ChannelHandler handler, URL url){
		this.handler = handler;
		this.url = url;
		//实例化具体序列化方式
		this.codec = getChannelCodec(url);
	}
	
	@Override
	public ChannelHandler getChannelHandler() {
		return handler;
	}



	protected static Codec2 getChannelCodec(URL url){
		return new TransportCodec();
	}
	
	public InetSocketAddress getConnectAddress() {
        return new InetSocketAddress(NetUtils.filterLocalHost(getUrl().getHost()), getUrl().getPort());
    }
	
	public void close() {
        closed = true;
    }
	
	 public boolean isClosed() {
	    return closed;
	 }
	 
	 public void send(Object message) throws Throwable{
		 send(message, this.url.getParameter("sent", false));
	 }
	
	@Override
	public void connected(Channel channel) throws Exception {
		if(closed){
			return;
		}
		this.handler.connected(channel);
	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		this.handler.disconnected(channel);
	}

	@Override
	public void sent(Channel channel, Object message) throws Exception {
		if(closed){
			return;
		}
		this.handler.sent(channel, message);
	}

	@Override
	public void received(Channel channel, Object message) throws Exception {
		if(closed){
			return;
		}
		this.handler.sent(channel, message);
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws Exception {
		this.handler.caught(channel, exception);
	}
	
	 public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Codec2 getCodec() {
		return codec;
	}
	
}
