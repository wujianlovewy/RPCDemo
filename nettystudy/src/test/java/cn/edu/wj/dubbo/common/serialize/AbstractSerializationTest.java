package cn.edu.wj.dubbo.common.serialize;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import org.junit.Test;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.common.ObjectInput;
import cn.edu.wj.rpc.dubbo.common.ObjectOutput;
import cn.edu.wj.rpc.dubbo.common.Serialization;

import static org.junit.Assert.fail;

public abstract class AbstractSerializationTest {

	Serialization serialization;
	URL url = new URL("protocl", "1.1.1.1", 1234);
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	@Test
	public void test_Bool() throws Exception {
		ObjectOutput objectOutput = serialization.serialize(url, byteArrayOutputStream);
		objectOutput.writeBool(false);
		objectOutput.flushBuffer();
		
		 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
	                byteArrayOutputStream.toByteArray());
		 
		 ObjectInput deserialize = serialization.deserialize(url, byteArrayInputStream);
		
		 System.out.println("序列化结果: "+deserialize.readBool());
		 System.out.println("序列化结果2: "+deserialize.readBool());
	}
	
	@Test
	public void test_Byte() throws Exception {
		ObjectOutput objectOutput = serialization.serialize(url, byteArrayOutputStream);
		objectOutput.writeByte((byte)123);
		objectOutput.flushBuffer();
		
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                byteArrayOutputStream.toByteArray());
	 
		ObjectInput deserialize = serialization.deserialize(url, byteArrayInputStream);
		assertEquals((byte) 123, deserialize.readByte());
		try {
			deserialize.readByte();
			fail();
		}catch(EOFException e){
			
		}
	}
}
