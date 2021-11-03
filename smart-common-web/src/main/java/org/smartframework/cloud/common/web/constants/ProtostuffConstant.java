package org.smartframework.cloud.common.web.constants;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

/**
 * protostuff常量
 *
 * @author collin
 * @date 2021-10-31
 */
public interface ProtostuffConstant {

    MediaType PROTOBUF_MEDIA_TYPE = new MediaType("application", "x-protobuf",
            StandardCharsets.UTF_8);

}