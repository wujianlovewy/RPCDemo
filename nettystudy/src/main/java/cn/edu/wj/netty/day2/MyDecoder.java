package cn.edu.wj.netty.day2;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

//自定义解码类
public class MyDecoder extends FrameDecoder{
	
	private final ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer channelBuffer) throws Exception {
		System.out.println("解码开始===>");
		if(channelBuffer.readableBytes()<4){
			return null;
		}
		
		if(channelBuffer.readable()){
			channelBuffer.readBytes(buffer, channelBuffer.readableBytes());
		}
		
		int namelen = buffer.readInt();
		String name = new String(buffer.readBytes(namelen).array(),"GBK");
		int age = buffer.readInt();
		double salary = buffer.readDouble();
		Person p = new Person();
		p.setName(name);
		p.setAge(age);
		p.setSalary(salary);
		System.out.println("解码结束,用户信息【name="+name+", age="+age+", salary="+salary+"】===>");
		return p;
	}
	


}
