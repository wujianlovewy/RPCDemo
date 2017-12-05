package cn.edu.wj.rpc.dubbo.netty;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;

public class NettyClient extends AbstractClient implements Client{

	// 因ChannelFactory的关闭有DirectMemory泄露，采用静态化规避
    // https://issues.jboss.org/browse/NETTY-424
    private static final ChannelFactory channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(new NamedThreadFactory("NettyClientBoss", true)),
            Executors.newCachedThreadPool(new NamedThreadFactory("NettyClientWorker", true)),
            Constants.DEFAULT_IO_THREADS);
    private ClientBootstrap bootstrap;

    private volatile Channel channel;
    
    private static final int TIME_OUT = 100000;
	
	public NettyClient(ChannelHandler channelHandler, URL url) throws Throwable{
		//调用父类
		super(channelHandler, url);
	}
    
    //netty连接初始化
	protected void doOpen() throws Throwable {
		bootstrap = new ClientBootstrap(channelFactory);
        // config
        // @see org.jboss.netty.channel.socket.SocketChannelConfig
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("connectTimeoutMillis", TIME_OUT);
        //创建nettyHandler
        final NettyHandler nettyHandler = new NettyHandler(this, this.getUrl());
        
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
            	NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyClient.this);
            	ChannelPipeline pipeline = Channels.pipeline();
            	pipeline.addLast("decoder", adapter.getDecoder());
            	pipeline.addLast("encoder", adapter.getEncoder());
            	pipeline.addLast("handler", nettyHandler);
            	return pipeline;
            }
        });
	 }

	@Override
	protected void doConnect() throws Throwable {
	     ChannelFuture future = bootstrap.connect(getConnectAddress());
	     try {
	    	 boolean ret = future.awaitUninterruptibly(5*1000, TimeUnit.MILLISECONDS);
	    	 if (ret && future.isSuccess()) {
	    		 Channel newChannel = future.getChannel();
	             newChannel.setInterestOps(Channel.OP_READ_WRITE);
	             try{
	            	 // 关闭旧的连接
	                 Channel oldChannel = NettyClient.this.channel; // copy reference
	                 if(oldChannel!=null){
	                	 try{
	                		 oldChannel.close();
	                	 }finally{
	                		 DubboChannel.removeChannelIfDisconnected(oldChannel);
	                	 }
	                 }
	             }finally{
	            	if(NettyClient.this.isClosed()){
	            		try{
	            			newChannel.close();
	            		}finally{
	            			DubboChannel.removeChannelIfDisconnected(newChannel);
	            			NettyClient.this.channel = null;
	            		}
	            	}else{
	            		NettyClient.this.channel = newChannel;
	            	}
	             }
	             
	    	 }else{
	    		 throw new Throwable("client Fail connect to Server");
	    	 }
	     } catch(Throwable t){
	    	 throw t;
	     }
	}

	@Override
	public cn.edu.wj.rpc.dubbo.netty.Channel getChannel() {
		Channel c = channel;
        if (c == null || !c.isConnected())
            return null;
		return DubboChannel.getOrAddChannel(c, this, getUrl());
	}
	
}
