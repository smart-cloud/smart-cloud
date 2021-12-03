/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartframework.cloud.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * jackson工具类
 *
 * @author liyulin
 * @date 2020-05-23
 */
@Slf4j
public final class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private JacksonUtil() {
    }

    /**
     * 对象转json
     *
     * @param value
     * @return
     */
    public static final String toJson(Object value) {
        String result = null;
        try {
            result = OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("write.String.error", e);
        }
        return result;
    }

    /**
     * 对象转json字节数组
     *
     * @param value
     * @return
     */
    public static final byte[] toBytes(Object value) {
        byte[] bytes = null;
        try {
            bytes = OBJECT_MAPPER.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            log.error("write.byte[].error", e);
        }
        return bytes;
    }

    /**
     * json转对象
     *
     * @param content
     * @param valueType
     * @return
     */
    public static <T> T parseObject(String content, Class<T> valueType) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

    /**
     * json转对象
     *
     * @param content
     * @param valueTypeRef
     * @return
     */
    public static <T> T parseObject(String content, TypeReference<T> valueTypeRef) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

    public static JsonNode parse(String content) {
        JsonNode t = null;
        try {
            t = OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

}