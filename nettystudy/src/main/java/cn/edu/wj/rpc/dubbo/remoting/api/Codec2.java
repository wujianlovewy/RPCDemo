package cn.edu.wj.rpc.dubbo.remoting.api;

import java.io.IOException;

import cn.edu.wj.rpc.dubbo.netty.Channel;

import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;

public interface Codec2 {

	public void encode(Channel channel, ChannelBuffer buffer, Object message)
			throws IOException;

	public Object decode(Channel channel, ChannelBuffer buffer)
			throws IOException;

	enum DecodeResult {
		NEED_MORE_INPUT, SKIP_SOME_INPUT
	}
}
