package cn.edu.wj.netty.day2;

import java.util.Scanner;
import org.jboss.netty.channel.Channel;

public class DemoTest {

	public static void main(String[] args) throws Exception {
		// 客户端的启动类  
        DemoClient client = new DemoClient();
        //连接服务端  
        client.init();
        Channel channel = client.connect();
        System.out.println("client start");  
        Scanner scanner = new Scanner(System.in);  
        while(true){  
            System.out.println("请输入");  
            channel.write(scanner.next());  
        }  
	}
	
}
