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
package io.github.smart.cloud.mask.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.smart.cloud.mask.jackson.MaskJacksonAnnotationIntrospector;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * jackson mask工具类
 *
 * @author collin
 * @date 2020-05-30
 */
@Slf4j
public final class JacksonMaskUtil {


    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .annotationIntrospector(new MaskJacksonAnnotationIntrospector())
            .addModule(new JavaTimeModule()).build();

    private JacksonMaskUtil() {
    }

    /**
     * 对象转json（脱敏序列化）
     *
     * @param value
     * @return
     */
    public static String mask(Object value) {
        if (value == null) {
            return null;
        }
        String result = null;
        if (value instanceof Serializable) {
            try {
                result = JSON_MAPPER.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                log.error("mask error", e);
            }
        } else {
            result = value.toString();
        }

        return result;
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

    public static JsonNode parseObject(String content) {
        JsonNode t = null;
        try {
            t = JSON_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

}