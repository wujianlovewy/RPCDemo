package cn.edu.wj.rpc.dubbo.remoting.exchange.support.header;

import java.util.Collection;

import com.alibaba.dubbo.remoting.exchange.support.header.HeaderExchangeHandler;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.Client;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Request;

//心跳检测类
final public class HeartBeatTask implements Runnable {

	private ChannelProvider channelProvider;
	
	// 心跳间隔时间
	private int heartbeat;

	// 心跳超时时间
	private int heartbeatTimeout;
	
	public HeartBeatTask(ChannelProvider channelProvider, int heartbeat, int heartbeatTimeout){
		this.channelProvider = channelProvider;
		this.heartbeat = heartbeat;
		this.heartbeatTimeout = heartbeatTimeout;
	}

	@Override
	public void run() {
	    try{
			long now = System.currentTimeMillis();
			for(Channel channel : this.channelProvider.getChannels()){
				if(channel.isClosed()){
					continue;
				}
				
				try{
					Long lastRead = (Long)channel.getAttribute(HeaderExchangeHandler.KEY_READ_TIMESTAMP);
					Long lastWrite = (Long)channel.getAttribute(HeaderExchangeHandler.KEY_WRITE_TIMESTAMP);
					
					//超过心跳间隔时间发起心跳数据包
					if((lastRead!=null && now-lastRead>heartbeat)
							|| (lastWrite!=null && now -lastWrite>this.heartbeat)){
						Request request = new Request();
						request.setVersion("2.0.0");
						request.setTwoWay(true);
						request.setEvent(Request.HEARTBEAT_EVENT);
						channel.send(request);
						System.out.println("Send heartbeat to remote channel " + channel.getRemoteAddress()
                                 + ", cause: The channel has no data-transmission exceeds a heartbeat period: " + heartbeat + "ms");
					}
					
					//心跳检测超时
					if(lastRead!=null && now-lastRead>heartbeatTimeout){
						if(channel instanceof Client){
							System.out.println("Reconnect Client channel " + channel
	                                + ", because heartbeat read idle time out: " + heartbeatTimeout + "ms");
							try {
								((Client) channel).reconnect();
							} catch (Exception e) {
							}
						}else{
							System.out.println("Close Server channel " + channel
	                                + ", because heartbeat read idle time out: " + heartbeatTimeout + "ms");
							channel.close();
						}
					}
					
				}catch(Throwable t){
					System.err.println("Exception when heartbeat to remote channel " 
							+channel.getRemoteAddress()+">>>"+ t.getMessage());
				}
			}
		}catch(Throwable t){
			System.err.println("Unhandled exception when heartbeat, cause: " + t.getMessage());
		}
	}
	
	public interface ChannelProvider {
		Collection<Channel> getChannels();
	}

}


