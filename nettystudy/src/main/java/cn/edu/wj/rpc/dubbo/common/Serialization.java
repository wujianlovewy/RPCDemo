package cn.edu.wj.rpc.dubbo.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.URL;


public interface Serialization {
	  /**
     * get content type id
     *
     * @return content type id
     */
    byte getContentTypeId();

    /**
     * get content type
     *
     * @return content type
     */
    String getContentType();

    /**
     * create serializer
     *
     * @param url
     * @param output
     * @return serializer
     * @throws IOException
     */
    ObjectOutput serialize(URL url, OutputStream output) throws IOException;

    /**
     * create deserializer
     *
     * @param url
     * @param input
     * @return deserializer
     * @throws IOException
     */
    ObjectInput deserialize(URL url, InputStream input) throws IOException;

}
