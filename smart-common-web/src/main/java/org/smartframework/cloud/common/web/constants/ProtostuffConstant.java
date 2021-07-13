package org.smartframework.cloud.common.web.constants;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public interface ProtostuffConstant {

	MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf",
			StandardCharsets.UTF_8);
	
}