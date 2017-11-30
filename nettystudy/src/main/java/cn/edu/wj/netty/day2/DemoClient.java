package cn.edu.wj.netty.day2;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
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
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;

public class DemoClient {
	static final String HOST = System.getProperty("host", "127.0.0.1");  
    static final int PORT = Integer.parseInt(System.getProperty("port", "9999"));  
    
    private static final ChannelFactory channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(new NamedThreadFactory("NettyClientBoss", true)),
            Executors.newCachedThreadPool(new NamedThreadFactory("NettyClientWorker", true)),
            Constants.DEFAULT_IO_THREADS);
    
    private ClientBootstrap bootstrap;
  
    public void init() throws Exception {  
    	
    	// 客户端的启动类  
        bootstrap = new  ClientBootstrap();  
        //线程池  
        ExecutorService boss = Executors.newCachedThreadPool();  
        ExecutorService worker = Executors.newCachedThreadPool();  
        //socket工厂  
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));  
        //管道工厂  
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {  
            @Override  
            public ChannelPipeline getPipeline() throws Exception {  
                ChannelPipeline pipeline = Channels.pipeline();  
                //pipeline.addLast("decoder", new StringDecoder());  
                //pipeline.addLast("encoder", new StringEncoder());  
                pipeline.addLast("decoder", new MyDecoder());  
                pipeline.addLast("encoder", new MyEncoder());  
                pipeline.addLast("hiHandler", new SimpleChannelClient());  
                return pipeline;  
            }  
        });  
        
    }  
    
    public Channel connect() throws Exception {
    	ChannelFuture future = bootstrap.connect(new InetSocketAddress(HOST, PORT));
    	boolean ret = future.awaitUninterruptibly(5*1000, TimeUnit.MILLISECONDS);
    	
		if (ret && future.isSuccess()) {
			 System.out.println("客户端建立连接成功!");
			 return future.getChannel();
		}else{
			 throw new Exception("客户端连接失败");
		}
    }
	
}
