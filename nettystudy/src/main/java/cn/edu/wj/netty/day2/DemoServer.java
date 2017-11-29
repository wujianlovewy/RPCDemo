package cn.edu.wj.netty.day2;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;

public class DemoServer {

	private int port;
	
	private ServerBootstrap bootstrap;
	
	public DemoServer(int port){this.port = port;}
	
	public void start(){
		//无界的Netty boss线程池，负责和消费者建立新的连接
		  ExecutorService boss = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerBoss", false));
		  // 无界的Netty worker线程池，负责连接的数据交换
		  ExecutorService worker = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerWorker", false));
		  //netty服务启动类
		  ChannelFactory channelFactory = new NioServerSocketChannelFactory(boss, worker);
	      bootstrap = new ServerBootstrap();
	      bootstrap.setFactory(channelFactory);
	      

	      bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
	            public ChannelPipeline getPipeline() {
	            	ChannelPipeline pipeline = Channels.pipeline();
	            	pipeline.addLast("decoder", new StringDecoder());  
	                pipeline.addLast("encoder", new StringEncoder());  
	                pipeline.addLast("handler", new SimpleChannelClient());  //添加一个Handler来处理客户端的事件，Handler需要继承ChannelHandler  
	            	return pipeline;
	            }
	      });
	      
	      bootstrap.bind(new InetSocketAddress(this.port)); 
		
        System.out.println("start!!!");  
	}
	
	
}
