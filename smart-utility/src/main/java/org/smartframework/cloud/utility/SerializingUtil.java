package org.smartframework.cloud.utility;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.io.InputStream;

/**
 * protostuff序列化工具类
 *
 * @author liyulin
 * @date 2019-12-02
 */
public final class SerializingUtil {

    private SerializingUtil() {
    }

    /**
     * 将目标类序列化为byte数组
     *
     * @param source
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T source) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) source.getClass());
        LinkedBuffer linkedBuffer = null;
        try {
            linkedBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            return ProtostuffIOUtil.toByteArray(source, schema, linkedBuffer);
        } finally {
            if (linkedBuffer != null) {
                linkedBuffer.clear();
            }
        }
    }

    /**
     * 将输入流反序列化为目标类
     *
     * @param input
     * @param typeClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T deserialize(InputStream input, Class<T> typeClass) throws IOException {
        Schema<T> schema = RuntimeSchema.getSchema(typeClass);
        T newInstance = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(input, newInstance, schema);
        return newInstance;
    }

    /**
     * 将输入流反序列化为目标类
     *
     * @param input
     * @param typeClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T deserialize(byte[] input, Class<T> typeClass) {
        Schema<T> schema = RuntimeSchema.getSchema(typeClass);
        T newInstance = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(input, newInstance, schema);
        return newInstance;
    }

}