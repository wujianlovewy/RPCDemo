package cn.edu.wj.rpc.dubbo.common.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.edu.wj.rpc.dubbo.common.ObjectOutput;

public class FastJsonObjectOutput implements ObjectOutput {

	private final PrintWriter writer;

	public FastJsonObjectOutput(OutputStream out) {
		this(new OutputStreamWriter(out));
	}

	public FastJsonObjectOutput(Writer writer) {
		this.writer = new PrintWriter(writer);
	}

	public void writeBool(boolean v) throws IOException {
		writeObject(v);
	}

	public void writeByte(byte v) throws IOException {
		writeObject(v);
	}

	public void writeShort(short v) throws IOException {
		writeObject(v);
	}

	public void writeInt(int v) throws IOException {
		writeObject(v);
	}

	public void writeLong(long v) throws IOException {
		writeObject(v);
	}

	public void writeFloat(float v) throws IOException {
		writeObject(v);
	}

	public void writeDouble(double v) throws IOException {
		writeObject(v);
	}

	public void writeUTF(String v) throws IOException {
		writeObject(v);
	}

	public void writeBytes(byte[] b) throws IOException {
		writer.println(new String(b));
	}

	public void writeBytes(byte[] b, int off, int len) throws IOException {
		writer.println(new String(b, off, len));
	}

	public void writeObject(Object obj) throws IOException {
		
		/***
		* 参考fastjson漏洞问题 https://www.cnblogs.com/fuyuanming/p/6626732.html
		* 解决办法 http://blog.csdn.net/cdyjy_litao/article/details/72458538
		**/
		//ParserConfig.getGlobalInstance().addAccept("cn.edu.wj"); //添加autotype白名单
		
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true); 
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.config(SerializerFeature.WriteEnumUsingToString, true);
		serializer.config(SerializerFeature.SortField, true);
        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
        serializer.config(SerializerFeature.WriteNullStringAsEmpty, true);
        serializer.config(SerializerFeature.WriteNullListAsEmpty, true);
		serializer.write(obj);
		out.writeTo(writer);
		out.close(); // for reuse SerializeWriter buf
		writer.println();
		writer.flush();
	}

	public void flushBuffer() throws IOException {
		writer.flush();
	}

}
