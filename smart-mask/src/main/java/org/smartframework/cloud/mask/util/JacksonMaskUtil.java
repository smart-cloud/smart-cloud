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
package org.smartframework.cloud.mask.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.mask.jackson.EnableMaskLogIntrospector;

import java.io.Serializable;

/**
 * jackson mask工具类
 *
 * @author liyulin
 * @date 2020-05-30
 */
@Slf4j
public final class JacksonMaskUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setAnnotationIntrospector(new EnableMaskLogIntrospector());
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * 对象转json（脱敏序列化）
     *
     * @param value
     * @return
     */
    public static final String mask(Object value) {
        if (value == null) {
            return null;
        }
        String result = null;
        if (value instanceof Serializable) {
            try {
                result = OBJECT_MAPPER.writeValueAsString(value);
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
            t = OBJECT_MAPPER.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

    public static JsonNode parseObject(String content) {
        JsonNode t = null;
        try {
            t = OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("parse object error", e);
        }

        return t;
    }

}