package cn.edu.wj.rpc.dubbo.remoting.exchange;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jwu
 * dubbo传输过程request对象
 */
public class Request {

	public static final String HEARTBEAT_EVENT = null;

    public static final String READONLY_EVENT = "R";

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    private final long mId;

    private String mVersion;

    private boolean mTwoWay = true;

    private boolean mEvent = false;

    private boolean mBroken = false;

    private Object mData;
    
    public Request() {
        mId = newId();
    }

    public Request(long id) {
        mId = id;
    }
    
    private static long newId() {
        // getAndIncrement()增长到MAX_VALUE时，再增长会变为MIN_VALUE，负数也可以做为ID
        return INVOKE_ID.getAndIncrement();
    }
	
}
