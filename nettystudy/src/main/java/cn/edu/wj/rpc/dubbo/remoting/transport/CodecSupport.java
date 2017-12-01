package cn.edu.wj.rpc.dubbo.remoting.transport;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.common.Serialization;
import cn.edu.wj.rpc.dubbo.common.support.FastJsonSerialization;

public class CodecSupport {

	public static Serialization getSerialization(URL url){
		return new FastJsonSerialization();
	}
	
}
