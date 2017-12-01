package cn.edu.wj.rpc.dubbo.common.support;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

import cn.edu.wj.rpc.dubbo.common.ObjectInput;

public class FastJsonObjectInput implements ObjectInput {
	
	private BufferedReader reader;
	
	public FastJsonObjectInput(InputStream in){
		this(new InputStreamReader(in));
	}
	
	public FastJsonObjectInput(Reader reader){
		this.reader = new BufferedReader(reader);
	}
	

	@Override
	public boolean readBool() throws IOException {
		try {
			return readObject(boolean.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public byte readByte() throws IOException {
		try {
			return readObject(byte.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public short readShort() throws IOException {
		try {
			return readObject(short.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public int readInt() throws IOException {
		try {
			return readObject(int.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public long readLong() throws IOException {
		try {
			return readObject(long.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public float readFloat() throws IOException {
		try {
			return readObject(float.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public double readDouble() throws IOException {
		try {
			return readObject(double.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public String readUTF() throws IOException {
		try {
			return readObject(String.class);
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public byte[] readBytes() throws IOException {
		return readLine().getBytes();
	}

	@Override
	public Object readObject() throws IOException, ClassNotFoundException {
		String json = this.readLine();
		return JSON.parse(json);
	}

	@Override
	public <T> T readObject(Class<T> cls) throws IOException,
			ClassNotFoundException {
		String json = this.readLine();
		return JSON.parseObject(json, cls);
	}

	@Override
	public <T> T readObject(Class<T> cls, Type type) throws IOException,
			ClassNotFoundException {
		return null;
	}
	
	private String readLine() throws IOException,EOFException {
		String line = this.reader.readLine();
		
		if(line==null || line.trim().length()==0) throw new EOFException("序列化数据读取异常");
		return line;
	}

}
