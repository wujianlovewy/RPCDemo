package cn.edu.wj.netty.day1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DemoClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("DemoClientHandler Active"); 
		/**
		 * 如果一个channelPipeline中有多个channelHandler时，且这些channelHandler中有同样的方法时，
		 * 例如这里的channelActive方法，只会调用处在第一个的channelHandler中的channelActive方法，
		 * 如果你想要调用后续的channelHandler的同名的方法就需要调用以“fire”为开头的方法了
		 */
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		 System.out.println("DemoClientHandler read Message:"+msg);  
		 //fireChannelRead方法才能调用后续channelHandler的channelRead方法
		 ctx.fireChannelRead(msg);
	}
	
	 @Override  
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
         cause.printStackTrace();  
         ctx.close();  
     }  

}
