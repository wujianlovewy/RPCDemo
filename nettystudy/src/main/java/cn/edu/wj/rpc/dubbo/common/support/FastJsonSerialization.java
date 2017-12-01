package cn.edu.wj.rpc.dubbo.common.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.URL;

import cn.edu.wj.rpc.dubbo.common.ObjectInput;
import cn.edu.wj.rpc.dubbo.common.ObjectOutput;
import cn.edu.wj.rpc.dubbo.common.Serialization;

public class FastJsonSerialization implements Serialization {

	@Override
	public byte getContentTypeId() {
		return 6;
	}

	@Override
	public String getContentType() {
		return "text/json";
	}

	@Override
	public ObjectOutput serialize(URL url, OutputStream output)
			throws IOException {
		return new FastJsonObjectOutput(output);
	}

	@Override
	public ObjectInput deserialize(URL url, InputStream input)
			throws IOException {
		return new FastJsonObjectInput(input);
	}

}
