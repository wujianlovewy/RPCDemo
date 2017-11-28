package cn.edu.wj.rpc.dubbo.netty;

public interface EndPoint {

	
	void send(Object message) throws Throwable;
	
	//sent 是否已经发送完成
	void send(Object message, boolean sent) throws Throwable;
	
}
