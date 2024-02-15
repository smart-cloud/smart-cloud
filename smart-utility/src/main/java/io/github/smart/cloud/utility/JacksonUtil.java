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
package io.github.smart.cloud.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

/**
 * jackson工具类
 *
 * @author collin
 * @date 2020-05-23
 */
@Slf4j
public final class JacksonUtil {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .addModule(new JavaTimeModule()).build();

    private JacksonUtil() {
    }

    /**
     * 对象转json
     *
     * @param value
     * @return
     */
    public static String toJson(Object value) {
        String result = null;
        try {
            result = JSON_MAPPER.writeValueAsString(value);
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
    public static byte[] toBytes(Object value) {
        byte[] bytes = null;
        try {
            bytes = JSON_MAPPER.writeValueAsBytes(value);
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
            t = JSON_MAPPER.readValue(content, valueType);
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
            t = JSON_MAPPER.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

    public static JsonNode parse(String content) {
        JsonNode t = null;
        try {
            t = JSON_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

}