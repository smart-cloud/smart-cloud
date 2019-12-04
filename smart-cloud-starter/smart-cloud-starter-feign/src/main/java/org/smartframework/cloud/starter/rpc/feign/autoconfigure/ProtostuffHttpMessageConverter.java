package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.smartframework.cloud.starter.rpc.feign.protostuff.SerializingUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

/**
 * @author liyulin
 * @desc protostuff格式转换
 * @date 2019/12/2
 */
public class ProtostuffHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

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
		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType != null && !PROTOBUF_MEDIA_TYPE.isCompatibleWith(contentType)) {
			logger.error("不支持的解码格式，请用x-protobuf作为contentType");
		}

		return SerializingUtil.deserialize(inputMessage.getBody(), clazz);
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException {
		MediaType contentType = outputMessage.getHeaders().getContentType();
		if (contentType != null && !PROTOBUF_MEDIA_TYPE.isCompatibleWith(contentType)) {
			logger.error("不支持的编码格式，请用x-protobuf作为contentType");
		}
		FileCopyUtils.copy(SerializingUtil.serialize(object), outputMessage.getBody());
	}

}