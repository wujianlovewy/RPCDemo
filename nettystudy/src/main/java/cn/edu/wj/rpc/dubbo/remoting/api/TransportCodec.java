package cn.edu.wj.rpc.dubbo.remoting.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.edu.wj.rpc.dubbo.common.ObjectInput;
import cn.edu.wj.rpc.dubbo.common.ObjectOutput;
import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.transport.AbstractCodec;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferInputStream;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferOutputStream;



public class TransportCodec extends AbstractCodec{

	//编码
	public void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException{
		OutputStream output = new ChannelBufferOutputStream(buffer);
		ObjectOutput objectOutput = getSerialization(channel).serialize(channel.getUrl(), output);
		this.encodeData(objectOutput, message);
		objectOutput.flushBuffer();
	} 
	
	//解码
	public Object decode(Channel channel, ChannelBuffer buffer) throws IOException{
		InputStream input = new ChannelBufferInputStream(buffer);
		
		ObjectInput objectInput = getSerialization(channel).deserialize(channel.getUrl(), input);
		return decodeData(channel, objectInput);
	}
	
	protected Object decodeData(Channel channel, ObjectInput objectInput) throws IOException{
		return decodeData(objectInput);
	}
	
	protected Object decodeData(ObjectInput objectInput) throws IOException{
		try {
			return objectInput.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("ClassNotFoundException: " + StringUtils.toString(e));
		}
	}
	
	protected void encodeData(Channel channel, ObjectOutput objectOutput, Object message) throws IOException{
		objectOutput.writeObject(message);
	}
	
	protected void encodeData(ObjectOutput objectOutput, Object message) throws IOException{
		objectOutput.writeObject(message);
	}
	
}
