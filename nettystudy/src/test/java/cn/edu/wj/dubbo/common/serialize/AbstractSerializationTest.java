package cn.edu.wj.dubbo.common.serialize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import org.junit.Test;

import cn.edu.wj.dubbo.common.serialize.model.BigPerson;
import cn.edu.wj.dubbo.common.serialize.model.PersonInfo;
import cn.edu.wj.dubbo.common.serialize.model.PersonStatus;
import cn.edu.wj.rpc.dubbo.common.ObjectInput;
import cn.edu.wj.rpc.dubbo.common.ObjectOutput;
import cn.edu.wj.rpc.dubbo.common.Serialization;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.model.Person;

public abstract class AbstractSerializationTest {

	Serialization serialization;
	URL url = new URL("protocl", "1.1.1.1", 1234);
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	BigPerson bigPerson = new BigPerson();
	{
		 	bigPerson = new BigPerson();
	        bigPerson.setPersonId("superman111");
	        bigPerson.setLoginName("superman");
	        bigPerson.setStatus(PersonStatus.ENABLED);
	        bigPerson.setEmail("sm@1.com");
	        bigPerson.setPenName("pname");
	        
	        PersonInfo pi = new PersonInfo();
	        pi.setMobileNo("13584652131");
	        pi.setMale(true);
	        pi.setDepartment("b2b");
	        pi.setHomepageUrl("www.capcom.com");
	        pi.setJobTitle("qa");
	        pi.setName("superman");
	        pi.setSalary(1020);
	        
	        bigPerson.setPersonInfo(pi);
	}
	

	@Test
	public void test_Bool() throws Exception {
		ObjectOutput objectOutput = serialization.serialize(url,
				byteArrayOutputStream);
		objectOutput.writeBool(false);
		objectOutput.flushBuffer();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				byteArrayOutputStream.toByteArray());

		ObjectInput deserialize = serialization.deserialize(url,
				byteArrayInputStream);

		System.out.println("序列化结果: " + deserialize.readBool());
		try {
			System.out.println("序列化结果2: " + deserialize.readBool());
			fail();
		} catch (EOFException e) {

		}
	}

	@Test
	public void test_Byte() throws Exception {
		ObjectOutput objectOutput = serialization.serialize(url,
				byteArrayOutputStream);
		objectOutput.writeByte((byte) 123);
		objectOutput.flushBuffer();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				byteArrayOutputStream.toByteArray());

		ObjectInput deserialize = serialization.deserialize(url,
				byteArrayInputStream);
		assertEquals((byte) 123, deserialize.readByte());
		try {
			deserialize.readByte();
			fail();
		} catch (EOFException e) {

		}
	}
	
	@Test
	public void test_Person() throws Exception {
		ObjectOutput objectOutput = serialization.serialize(url,
				byteArrayOutputStream);
		objectOutput.writeObject(new Person());
	}
	
	<T> void assertObject(T data) throws Exception {
		ObjectOutput objectOutput = serialization.serialize(url,
				byteArrayOutputStream);
		objectOutput.writeObject(data);
		objectOutput.flushBuffer();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				byteArrayOutputStream.toByteArray());
		ObjectInput deserialize = serialization.deserialize(url,
				byteArrayInputStream);

		assertEquals(data, (T) deserialize.readObject());

		try {
			deserialize.readObject();
			fail();
		} catch (IOException expected) {
		}
	}
}
