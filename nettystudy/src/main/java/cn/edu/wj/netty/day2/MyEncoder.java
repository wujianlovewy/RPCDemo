package cn.edu.wj.netty.day2;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

//自定义编码类
public class MyEncoder extends OneToOneEncoder{

	private final ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		System.out.println(Thread.currentThread().getName()+"---编码开始===>");
		if((msg instanceof ChannelBuffer)){
			return msg;
		}
		
		Person person = (Person)msg;
		buffer.writeInt(person.getName().getBytes("GBK").length);
		buffer.writeBytes(person.getName().getBytes("GBK"));
		buffer.writeInt(person.getAge());
		buffer.writeDouble(person.getSalary());
		
		return ChannelBuffers.wrappedBuffer(buffer.array());
	}

}
