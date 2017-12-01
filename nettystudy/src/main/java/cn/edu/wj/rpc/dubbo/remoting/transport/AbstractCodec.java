package cn.edu.wj.rpc.dubbo.remoting.transport;

import cn.edu.wj.rpc.dubbo.common.Serialization;
import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.api.Codec2;

public abstract class AbstractCodec implements Codec2 {

	protected Serialization getSerialization(Channel channel){
		return CodecSupport.getSerialization(channel.getUrl());
	}
	
}
