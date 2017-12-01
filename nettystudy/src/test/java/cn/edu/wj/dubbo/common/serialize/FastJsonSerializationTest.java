package cn.edu.wj.dubbo.common.serialize;

import org.junit.Test;

import cn.edu.wj.netty.day2.Person;
import cn.edu.wj.rpc.dubbo.common.support.FastJsonSerialization;

public class FastJsonSerializationTest extends AbstractSerializationTest {
	
	{ 
		this.serialization = new FastJsonSerialization();
	}

	@Test
	public void assertObject() throws Exception {
		assertObject(new Person(12,"张三",1200));
	}
	
}
