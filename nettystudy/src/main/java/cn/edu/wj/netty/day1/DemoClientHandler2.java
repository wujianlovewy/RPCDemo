package cn.edu.wj.netty.day1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DemoClientHandler2 extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("DemoClientHandler2 Active");  
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		 System.out.println("DemoClientHandler2 read Message:"+msg);  
		 ctx.fireChannelRead(msg);
	}

}
