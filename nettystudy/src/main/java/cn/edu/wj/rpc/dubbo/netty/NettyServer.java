package cn.edu.wj.rpc.dubbo.netty;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;

public class NettyServer extends AbstractServer implements Server{

   private Map<String, Channel> channels; // <ip:port, channel>

    private ServerBootstrap bootstrap;

    private org.jboss.netty.channel.Channel channel;
	
	public NettyServer(ChannelHandler handler, URL url) throws Throwable {
		super(handler, url);
	}

	@Override
	protected void doOpen() throws Throwable {
		  //无界的Netty boss线程池，负责和消费者建立新的连接
		  ExecutorService boss = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerBoss", true));
		  // 无界的Netty worker线程池，负责连接的数据交换
		  ExecutorService worker = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerWorker", true));
		  //netty服务启动类
		  ChannelFactory channelFactory = new NioServerSocketChannelFactory(boss, worker, getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS));
	      bootstrap = new ServerBootstrap(channelFactory);
	      final NettyHandler nettyHandler = new NettyHandler(this, this.getUrl());
	      
	      channels = nettyHandler.getChannels();
	      
	      bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
	            public ChannelPipeline getPipeline() {
	            	NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyServer.this);
	            	ChannelPipeline pipeline = Channels.pipeline();
	            	pipeline.addLast("decoder", adapter.getDecoder());
	            	pipeline.addLast("encoder", adapter.getEncoder());
	            	pipeline.addLast("handler", nettyHandler);
	            	return pipeline;
	            }
	      });
	      
	      //bind
	      channel = bootstrap.bind(getBindAddress());
	}

	@Override
	public boolean isConnected() {
		return false;
	}

}
