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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Writer;

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
     * 对象转json（限制最大长度）
     *
     * <p>
     * 输出达到maxLength后立即中止序列化，避免大对象（如大结果集）完整序列化后再截断带来的CPU和内存开销。
     * 被截断时返回的内容不是完整的json，仅适用于日志打印等场景。
     *
     * @param value     待序列化对象
     * @param maxLength 最大长度
     * @return json字符串，长度不超过maxLength；序列化失败时返回null
     */
    public static String toJson(Object value, int maxLength) {
        if (maxLength <= 0) {
            return "";
        }

        LimitedStringWriter writer = new LimitedStringWriter(maxLength);
        try {
            JSON_MAPPER.writeValue(writer, value);
        } catch (IOException | RuntimeException e) {
            // 达到长度上限主动中止的序列化，直接返回已写入的内容
            if (writer.isOverflow()) {
                return writer.toString();
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            log.error("write.String.error", e);
            return null;
        }
        return writer.toString();
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

    /**
     * 限制最大长度的Writer，超出长度时抛出异常以中止序列化
     */
    private static final class LimitedStringWriter extends Writer {

        private static final int MAX_INITIAL_CAPACITY = 256;
        private final StringBuilder buffer;
        private final int maxLength;
        private boolean overflow;

        LimitedStringWriter(int maxLength) {
            this.maxLength = maxLength;
            this.buffer = new StringBuilder(Math.min(maxLength, MAX_INITIAL_CAPACITY));
        }

        boolean isOverflow() {
            return overflow;
        }

        @Override
        public void write(int c) throws IOException {
            if (overflow) {
                return;
            }
            if (buffer.length() >= maxLength) {
                abort();
            }
            buffer.append((char) c);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (overflow) {
                return;
            }
            int remaining = maxLength - buffer.length();
            if (len <= remaining) {
                buffer.append(cbuf, off, len);
                return;
            }
            buffer.append(cbuf, off, remaining);
            abort();
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
            if (overflow) {
                return;
            }
            int remaining = maxLength - buffer.length();
            if (len <= remaining) {
                buffer.append(str, off, off + len);
                return;
            }
            buffer.append(str, off, off + remaining);
            abort();
        }

        /**
         * 标记溢出并中止序列化。溢出后的写入（如generator关闭时的flush）直接忽略，不再抛异常
         *
         * @throws IOException
         */
        private void abort() throws IOException {
            overflow = true;
            throw new LengthExceededException();
        }

        @Override
        public void flush() {
            // 内存写入，无需flush
        }

        @Override
        public void close() {
            // 内存写入，无需关闭
        }

        @Override
        public String toString() {
            return buffer.toString();
        }
    }

    /**
     * 长度超限异常（不收集堆栈，降低开销）
     */
    private static final class LengthExceededException extends IOException {

        private static final long serialVersionUID = 1L;

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

}