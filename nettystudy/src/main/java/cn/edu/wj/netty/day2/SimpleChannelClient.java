package cn.edu.wj.netty.day2;


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 使用netty3 SimpleChannelHandler实现客户端
 * @author jwu
 */
public class SimpleChannelClient extends SimpleChannelHandler{

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		System.out.println(ctx.getName()+ctx.getChannel().getLocalAddress()+"_接收消息: "+e.getMessage());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.writeRequested(ctx, e);
		System.out.println(ctx.getName()+ctx.getChannel().getLocalAddress()+"_发送消息: "+e.getMessage());
	}

	
	
	
}
