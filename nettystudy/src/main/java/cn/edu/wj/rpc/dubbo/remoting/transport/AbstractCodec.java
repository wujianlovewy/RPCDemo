package cn.edu.wj.rpc.dubbo.remoting.transport;

import java.io.IOException;

import com.alibaba.dubbo.common.Constants;

import cn.edu.wj.rpc.dubbo.common.Serialization;
import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.api.Codec2;

public abstract class AbstractCodec implements Codec2 {

	protected static void checkPayload(Channel channel, long size)
			throws IOException {
		int payload = Constants.DEFAULT_PAYLOAD;
		if (channel != null && channel.getUrl() != null) {
			payload = channel.getUrl().getParameter(Constants.PAYLOAD_KEY,
					Constants.DEFAULT_PAYLOAD);
		}
		if (payload > 0 && size > payload) {
			IOException e = new IOException(
					"Data length too large: " + size + ", max payload: "
							+ payload + ", channel: " + channel);
			throw e;
		}
	}

	protected Serialization getSerialization(Channel channel) {
		return CodecSupport.getSerialization(channel.getUrl());
	}

}
