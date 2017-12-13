package cn.edu.wj.dubbo.remoting.exchange.support.header;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Request;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.header.HeartBeatTask;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.exchange.support.header.HeaderExchangeHandler;

public class HeartBeatTaskTest {

	private URL url = URL.valueOf("dubbo://localhost:20880");

	private MockChannel channel;
	private HeartBeatTask task;

	@Before
    public void setup() throws Exception {
        task = new HeartBeatTask(new HeartBeatTask.ChannelProvider() {
        	public Collection<Channel> getChannels() {
                return Collections.<Channel>singletonList(channel);
            }
        }, 1000, 1000 * 3);

        channel = new MockChannel() {
            @Override
            public URL getUrl() {
                return url;
            }
        };
    }

    @Test
    public void testHeartBeat() throws Exception {
        url = url.addParameter(Constants.DUBBO_VERSION_KEY, "2.1.1");
        channel.setAttribute(
                HeaderExchangeHandler.KEY_READ_TIMESTAMP, System.currentTimeMillis());
        channel.setAttribute(
                HeaderExchangeHandler.KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
        Thread.sleep(2000L);
        task.run();
        List<Object> objects = channel.getSentObjects();
        Assert.assertTrue(objects.size() > 0);
        Object obj = objects.get(0);
        Assert.assertTrue(obj instanceof Request);
        Request request = (Request) obj;
        Assert.assertTrue(request.isHeartbeat());
    }
}
