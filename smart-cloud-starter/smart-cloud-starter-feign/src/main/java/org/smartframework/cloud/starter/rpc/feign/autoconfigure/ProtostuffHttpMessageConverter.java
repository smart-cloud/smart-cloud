package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.starter.rpc.feign.protostuff.SerializingUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author liyulin
 * @desc
 * @date 2019/12/2
 */
@Slf4j
public class ProtostuffHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    public static final MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf", StandardCharsets.UTF_8);

    public ProtostuffHttpMessageConverter() {
        super(new MediaType[]{PROTOBUF_MEDIA_TYPE, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML});
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (contentType != null && !PROTOBUF_MEDIA_TYPE.isCompatibleWith(contentType)) {
            logger.error("不支持的解码格式，请用x-protobuf作为contentType");
        }

        return SerializingUtil.deserialize(inputMessage.getBody(), clazz);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        if (contentType != null && !PROTOBUF_MEDIA_TYPE.isCompatibleWith(contentType)) {
            logger.error("不支持的编码格式，请用x-protobuf作为contentType");
        }
        FileCopyUtils.copy(SerializingUtil.serialize(object), outputMessage.getBody());
    }

}