package cn.edu.wj.rpc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.wj.rpc.demo.RpcProxy;

/**
 * 参考https://gitee.com/huangyong/rpc
 * @author jwu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/rpc/rpc-server.xml")
public class RpcServiceTest {

	@Autowired
    private RpcProxy rpcProxy;

    @Test
    public void helloTest() {
    }
	
}
