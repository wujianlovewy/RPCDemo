package cn.edu.wj.rpc.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.rpc.demo.RpcProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/rpc/rpc-client.xml")
public class RpcClientTest { 

	@Autowired
    private RpcProxy rpcProxy;
	
	@Test
	public void helloTest3(){
		
        int threadNum = 60;
        int loopCount = 20;

        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        final CountDownLatch latch = new CountDownLatch(loopCount);

        try {
        	long start = System.currentTimeMillis();
            for (int i = 0; i < loopCount; i++) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        HelloService helloService = rpcProxy.create(HelloService.class);
                        String result = helloService.sayHi("World");
                        System.out.println(result);
                        latch.countDown();
                    }
                });
            }
            latch.await();

            long time = System.currentTimeMillis() - start;
            System.out.println("thread: " + threadNum);
            System.out.println("loop: " + loopCount);
            System.out.println("time: " + time + "ms");
            System.out.println("tps: " + (double) loopCount / ((double) time / 1000));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        
        System.exit(0);
	}

	@Test
	public void helloTest2(){
		HelloService helloService = rpcProxy.create(HelloService.class);
		int loopCount = 100;

        long start = System.currentTimeMillis();

        for (int i = 0; i < loopCount; i++) {
            String result = helloService.sayHi("World");
            System.out.println(result);
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("loop: " + loopCount);
        System.out.println("time: " + time + "ms");
        System.out.println("tps: " + (double) loopCount / ((double) time / 1000));

        System.exit(0);
	}
	
    @Test
    public void helloTest() {
    	HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.sayHi("hello rpc!");
        System.out.println("Rpc result:"+result);
    }
	
}
