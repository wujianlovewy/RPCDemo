package cn.edu.wj.rpc.dubbo.common;

import java.io.IOException;

public interface ObjectOutput extends DataOutput {

	void writeObject(Object object) throws IOException;
	
}
