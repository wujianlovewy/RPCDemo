package cn.edu.wj.rpc.dubbo.remoting.api;

import java.io.IOException;
import java.io.InputStream;

import cn.edu.wj.rpc.dubbo.common.ObjectInput;
import cn.edu.wj.rpc.dubbo.common.ObjectOutput;
import cn.edu.wj.rpc.dubbo.common.Serialization;
import cn.edu.wj.rpc.dubbo.netty.Channel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Request;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Response;
import cn.edu.wj.rpc.dubbo.remoting.transport.CodecSupport;

import com.alibaba.dubbo.common.io.Bytes;
import com.alibaba.dubbo.common.io.StreamUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferInputStream;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferOutputStream;

/**
 * @author wuj
 * dubbo自定义head信息
  byte 16
	0-1 magic code
	2 flag
	 8 - 1-request/0-response
	 7 - two way
	 6 - heartbeat
	 1-5 serialization id
	3 status
		20 ok
		90 error
	4-11 id (long)
	12 -15 datalength
 */
public class ExchangeCodec extends TelnetCodec {
	
	 // header length.
    protected static final int HEADER_LENGTH = 16;
    // magic header.
    protected static final short MAGIC = (short) 0xdabb;
    protected static final byte MAGIC_HIGH = Bytes.short2bytes(MAGIC)[0];
    protected static final byte MAGIC_LOW = Bytes.short2bytes(MAGIC)[1];
    // message flag.
    protected static final byte FLAG_REQUEST = (byte) 0x80;  //10000000
    protected static final byte FLAG_TWOWAY = (byte) 0x40;   //01000000
    protected static final byte FLAG_EVENT = (byte) 0x20;    //00100000
    protected static final int SERIALIZATION_MASK = 0x1f;    //00011111
    
    public void encode(Channel channel, ChannelBuffer buffer, Object msg) throws IOException {
    	if(msg instanceof Request){
    		this.encodeRequest(channel, buffer, (Request)msg);
    	}else if(msg instanceof Response){
    		this.encodeResponse(channel, buffer, (Response)msg);
    	}else{
    		super.encode(channel, buffer, msg);
    	}
    }
    
    public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
    	int readable = buffer.readableBytes();
    	byte[] header = new byte[Math.min(readable, HEADER_LENGTH)];
    	buffer.readBytes(header);
    	return this.decode(channel, buffer, readable, header);
	}
    
    protected Object decode(Channel channel, ChannelBuffer buffer, int readable, byte[] header) throws IOException{
    	
    	//校验magic code
    	if(readable>0 && header[0]!=MAGIC_HIGH 
    			|| readable>1 && header[1]!=MAGIC_LOW){
    		int length = header.length;
    		if(header.length < readable){
    			header = Bytes.copyOf(header, readable);
    			buffer.readBytes(header, length, readable-length);
    		}
    		return super.decode(channel, buffer, readable, header);
    	}
    	
    	if(readable < HEADER_LENGTH){
    		return DecodeResult.NEED_MORE_INPUT;
    	}
    	
    	// get data length.
        int len = Bytes.bytes2int(header, 12);
        checkPayload(channel, len);
        
        int tt = len + HEADER_LENGTH;
        if(readable < tt) {
        	return DecodeResult.NEED_MORE_INPUT;
        }
        
        ChannelBufferInputStream is = new ChannelBufferInputStream(buffer, len);
    	try{
    		return this.decodeBody(channel, is, header);
    	}finally{
    		if (is.available() > 0) {
                try {
                   	System.out.println("Skip input stream " + is.available());
                    StreamUtils.skipUnusedStream(is);
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
    	}
    }
    
    protected Object decodeBody(Channel channel, InputStream is, byte[] header) throws IOException {
    	byte flag = header[2], proto = (byte)(flag & SERIALIZATION_MASK);
    	Serialization serialization = CodecSupport.getSerialization(channel.getUrl(), proto);
    	ObjectInput in = serialization.deserialize(channel.getUrl(), is);

    	long id = Bytes.bytes2long(header, 4);
    	
    	if((flag & FLAG_REQUEST)==0){
    		// decode response.
    		Response resp = new Response(id);
    		if((flag & FLAG_EVENT) != 0) resp.setEvent(Request.HEARTBEAT_EVENT);
    		
    		byte status = header[3];
    		resp.setStatus(status);
    		if (status == Response.OK) {
    			try{
        			Object data = in.readObject();
        			resp.setResult(data);
        		}catch(Throwable t){
        			//异常request处理
        			resp.setStatus(Response.CLIENT_ERROR);
                    resp.setErrorMessage(StringUtils.toString(t));
        		}
    		}else{
    			resp.setErrorMessage(in.readUTF());
    		}
    	   	return resp;
    	}else{
    		Request req = new Request(id);
    		req.setVersion("2.0.0");
    		req.setTwoWay((flag & FLAG_TWOWAY) != 0);
    		if((flag & FLAG_EVENT) != 0) req.setEvent(Request.HEARTBEAT_EVENT);
    		
    		try{
    			Object data = in.readObject();
    			req.setData(data);
    		}catch(Throwable t){
    			//异常request处理
    			req.setBroken(true);
    			req.setData(t);
    		}
    	   	return req;
    	}
    }
    
    //对response转码
    protected void encodeResponse(Channel channel, ChannelBuffer buffer, Response resp) throws IOException{
    	//获取序列化对象
    	Serialization serialization = getSerialization(channel);
    	
    	//组装Header信息
    	byte[] header = new byte[HEADER_LENGTH];
    	
    	// set magic number.
        Bytes.short2bytes(MAGIC, header);
        
        // set response and serialization flag.
        header[2] = (byte) (serialization.getContentTypeId());
        
        if(resp.isHeartbeat()) header[2] |= FLAG_EVENT;
        
        byte status = resp.getStatus();
        header[3] = status;
        
        Bytes.long2bytes(resp.getId(), header, 4);
        
        int savedWriteIndex = buffer.writerIndex();
        buffer.writerIndex(savedWriteIndex+HEADER_LENGTH);
        
        ChannelBufferOutputStream bos = new ChannelBufferOutputStream(buffer);
        ObjectOutput output = serialization.serialize(channel.getUrl(), bos);
        
        if(status == Response.OK){
        	this.encodeResponseData(channel, output, resp.getResult());
        }else{
        	output.writeUTF(resp.getErrorMessage());
        }
        
        output.flushBuffer();
        bos.flush();
        bos.close();
        
        int len = bos.writtenBytes();
        
        //校验传输字节长度
        checkPayload(channel, len);
        Bytes.int2bytes(len, header, 12);
        
        buffer.writerIndex(savedWriteIndex);
        buffer.writeBytes(header);
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
    }
    
    protected void encodeResponseData(Channel channel, ObjectOutput output, Object data) throws IOException{
    	this.encodeResponseData(output, data);
    }
    
    protected void encodeResponseData(ObjectOutput output, Object data) throws IOException{
    	output.writeObject(data);
    }
    
    //对request转码
    protected void encodeRequest(Channel channel, ChannelBuffer buffer, Request req) throws IOException{
    	//获取序列化对象
    	Serialization serialization = getSerialization(channel);
    	
    	//组装Header信息
    	byte[] header = new byte[HEADER_LENGTH];
    	
    	// set magic number.
        Bytes.short2bytes(MAGIC, header);
    	
        // set request and serialization flag.
        header[2] = (byte) (FLAG_REQUEST |(byte)serialization.getContentTypeId());
        
        if(req.isTwoWay()) header[2]|= FLAG_TWOWAY;
        if(req.isEvent())  header[2]|=FLAG_EVENT;
        
        //设置requestID
        Bytes.long2bytes(req.getId(), header, 4);
        
        //encode request data
        int savedWriteIndex = buffer.writerIndex();
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);
        
        ChannelBufferOutputStream bos = new ChannelBufferOutputStream(buffer);
        ObjectOutput output = serialization.serialize(channel.getUrl(), bos);
        
        this.encodeRequestData(channel, output, req.getData());
        
        output.flushBuffer();
        bos.flush();
        bos.close();
        
        int len = bos.writtenBytes();
        //校验传输字节长度
        checkPayload(channel, len);
        Bytes.int2bytes(len, header, 12);
        
        buffer.writerIndex(savedWriteIndex);
        buffer.writeBytes(header);
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
    }
    
    protected void encodeRequestData(Channel channel, ObjectOutput output, Object data) throws IOException{
    	this.encodeRequestData(output, data);
    }
    
    protected void encodeRequestData(ObjectOutput output, Object data) throws IOException{
    	output.writeObject(data);
    }
    
    
    public static void main(String[] args) {
		
    	//encode test:
    	// header.
        byte[] header = new byte[HEADER_LENGTH];
        // set magic number.
        Bytes.short2bytes(MAGIC, header);
        
        // set request and serialization flag.
        header[2] = (byte) (FLAG_REQUEST |(byte)6);
        
        header[2] |= FLAG_TWOWAY;
        header[2] |= FLAG_EVENT;
        
        // set request id.
        Bytes.long2bytes(123456, header, 4);
    	
        
        byte flag = header[2], proto = (byte) (flag & SERIALIZATION_MASK);
        long id = Bytes.bytes2long(header, 4);
        if ((flag & FLAG_REQUEST) == 0) {
        	System.out.println("decode response");
        	if ((flag & FLAG_EVENT) != 0) {
        		System.out.println("decode heartbeat response");
        	}
        }else{
        	System.out.println("decode request");
        	System.out.println("istwoway: "+((flag & FLAG_TWOWAY) != 0));
        	if ((flag & FLAG_EVENT) != 0) {
        		System.out.println("decode heartbeat request");
        	}
        }
	}
	
}
