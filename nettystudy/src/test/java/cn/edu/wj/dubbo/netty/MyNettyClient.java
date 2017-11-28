package cn.edu.wj.dubbo.netty;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.RemotingException;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.netty.ChannelHandler;
import cn.edu.wj.rpc.dubbo.netty.NettyClient;
import cn.edu.wj.rpc.dubbo.netty.NettyServer;


public class MyNettyClient {
	
	public static void main(String[] args) throws Throwable {
		int port = 55555;
		URL clientURL = URL.valueOf("netty://127.0.0.1").setPort(port);
		URL serverURL = URL.valueOf("netty://127.0.0.1").setPort(port);
		
		MyHandler clientHandler = new MyHandler(String.valueOf(port), true);
		MyHandler serverHandler = new MyHandler(String.valueOf(port), false);
		
		NettyServer nettyServer = new NettyServer(serverHandler, serverURL);
		NettyClient nettyClient = new NettyClient(clientHandler, clientURL);
		
		nettyClient.send("nihao i am client",true);
		
		Thread.sleep(5000);
	}
	
	 static class MyHandler implements ChannelHandler {

	        private String message;
	        private boolean success;
	        private boolean client;

	        MyHandler(String msg, boolean client) {
	            message = msg;
	            this.client = client;
	        }

	        public boolean isSuccess() {
	            return success;
	        }

	        private void checkThreadName() {
	            if (!success) {
	                success = Thread.currentThread().getName().contains(message);
	            }
	        }

	        private void output(String method) {
	            System.out.println(Thread.currentThread().getName()
	                    + " " + (client ? "client " + method : "server " + method));
	        }

	        public void connected(Channel channel) throws RemotingException {
	            output("connected");
	            checkThreadName();
	        }

	        public void disconnected(Channel channel) throws RemotingException {
	            output("disconnected");
	            checkThreadName();
	        }

	        public void sent(Channel channel, Object message) throws RemotingException {
	            output("sent");
	            checkThreadName();
	        }

	        public void received(Channel channel, Object message) throws RemotingException {
	            output("received");
	            checkThreadName();
	        }

	        public void caught(Channel channel, Throwable exception) throws RemotingException {
	            output("caught");
	            checkThreadName();
	        }
	    }

	
}
