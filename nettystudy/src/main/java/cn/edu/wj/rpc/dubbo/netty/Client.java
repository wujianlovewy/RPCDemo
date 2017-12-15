package cn.edu.wj.rpc.dubbo.netty;

public interface Client extends Channel,EndPoint{

	void reconnect() throws Exception;
	
}
