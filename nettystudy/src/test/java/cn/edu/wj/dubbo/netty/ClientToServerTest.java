package cn.edu.wj.dubbo.netty;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeServer;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ResponseFuture;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.Replier;

import com.alibaba.dubbo.remoting.RemotingException;

/**
 * @author jwu
 * 参考 ： Dubbo源码分析   http://blog.csdn.net/lang_man_xing/article/details/51459685
 *	http://blog.csdn.net/joeyon1985/article/details/51046599
 *	http://blog.csdn.net/pentiumchen/article/details/53227844
 */
public abstract class ClientToServerTest extends TestCase{

	 protected static final String LOCALHOST = "127.0.0.1";
	 
	 protected ExchangeServer server;

	    protected ExchangeChannel client;

	    protected WorldHandler handler = new WorldHandler();

	    protected abstract ExchangeServer newServer(int port, Replier<?> receiver) throws Exception;

	    protected abstract ExchangeChannel newClient(int port) throws Exception;

	    @Override
	    protected void setUp() throws Exception {
	        super.setUp();
	        int port = (int) (1000 * Math.random() + 10000);
	        server = newServer(port, handler);
	        client = newClient(port);
	    }

	    @Override
	    protected void tearDown() throws Exception {
	        super.tearDown();
	        try {
	            if (server != null)
	                server.close();
	        } finally {
	            if (client != null)
	                client.close();
	        }
	    }

	    @Test
	    public void testFuture() throws Exception {
	        ResponseFuture future = client.request(new World("world"));
	        Hello result = (Hello) future.get();
	        Assert.assertEquals("hello,world", result.getName());
	        System.out.println("result: "+result.getName());
	    }

//	    @Test
//	    public void testCallback() throws Exception {
//	        final Object waitter = new Object();
//	        client.invoke(new World("world"), new InvokeCallback<Hello>() {
//	            public void callback(Hello result) {
//	                Assert.assertEquals("hello,world", result.getName());
//	                synchronized (waitter) {
//	                    waitter.notifyAll();
//	                }
//	            }
//	            public void onException(Throwable exception) {
//	            }
//	        });
//	        synchronized (waitter) {
//	            waitter.wait();
//	        }
//	    }
}
