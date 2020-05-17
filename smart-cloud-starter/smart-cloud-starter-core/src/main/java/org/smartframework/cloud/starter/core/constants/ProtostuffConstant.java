package org.smartframework.cloud.starter.core.constants;

import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;

public interface ProtostuffConstant {

	MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf",
			StandardCharsets.UTF_8);
	
}