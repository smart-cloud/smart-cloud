package org.smartframework.cloud.starter.rpc.feign.converter;

import org.smartframework.cloud.utility.SerializingUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author liyulin
 * @desc protostuff格式转换
 * @date 2019/12/2
 */
public class ProtostuffHttpMessageConverter extends AbstractHttpMessageConverter<Object>
        implements GenericHttpMessageConverter<Object> {

    public static final MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf",
            StandardCharsets.UTF_8);

    public ProtostuffHttpMessageConverter() {
        super(PROTOBUF_MEDIA_TYPE, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        return SerializingUtil.deserialize(inputMessage.getBody(), clazz);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException {
        FileCopyUtils.copy(SerializingUtil.serialize(object), outputMessage.getBody());
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return mediaType != null && PROTOBUF_MEDIA_TYPE.isCompatibleWith(mediaType);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        Class<?> c = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            c = (Class<?>) parameterizedType.getRawType();
            return SerializingUtil.deserialize(inputMessage.getBody(), c);
        } else {
            c = (Class<?>) type;
        }
        return SerializingUtil.deserialize(inputMessage.getBody(), c);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return mediaType != null && PROTOBUF_MEDIA_TYPE.isCompatibleWith(mediaType);
    }

    @Override
    public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        FileCopyUtils.copy(SerializingUtil.serialize(t), outputMessage.getBody());
    }

}