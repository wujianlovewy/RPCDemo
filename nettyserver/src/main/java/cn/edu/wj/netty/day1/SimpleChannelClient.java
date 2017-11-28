package cn.edu.wj.netty.day1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 使用SimpleChannelInboundHandler实现客户端
 * @author jwu
 *
 */
public class SimpleChannelClient extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("SimpleChannelClient read Message:"+msg);  
	}
	

	 @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 System.out.println("SimpleChannelClient channelActive");  
	}



	@Override  
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
         cause.printStackTrace();  
         ctx.close();  
     }  
}
