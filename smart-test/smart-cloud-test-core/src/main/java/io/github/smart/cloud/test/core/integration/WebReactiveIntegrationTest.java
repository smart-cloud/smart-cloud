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
package io.github.smart.cloud.test.core.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * web reactive集成测试父类
 *
 * @author collin
 * @date 2020-09-07
 */
@Slf4j
public class WebReactiveIntegrationTest extends AbstractIntegrationTest implements IIntegrationTest {

    protected WebTestClient webTestClient;

    @BeforeEach
    public void initMock() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Override
    public <T> T post(String url, Object req, TypeReference<T> typeReference) throws Exception {
        return post(url, null, req, typeReference);
    }

    @Override
    public <T> T post(String url, Map<String, String> headers, Object req, TypeReference<T> typeReference) throws Exception {
        String requestJsonStr = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);
        WebTestClient.RequestBodyUriSpec requestBodyUriSpec = webTestClient.post();
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBodyUriSpec::header);
        }
        WebTestClient.RequestBodySpec requestBodySpec = requestBodyUriSpec.uri(url)
                .contentType(MediaType.APPLICATION_JSON);
        if (req != null) {
            requestBodySpec.bodyValue(req);
        }
        byte[] resultBytes = requestBodySpec.acceptCharset(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .returnResult()
                .getResponseBody();

        return deserializeResponse(resultBytes, typeReference, url);
    }

    @Override
    public <T> T get(String url, Object req, TypeReference<T> typeReference) throws Exception {
        return get(url, null, req, typeReference);
    }

    @Override
    public <T> T get(String url, Map<String, String> headers, Object req, TypeReference<T> typeReference) throws Exception {
        String requestJsonStr = (req == null) ? null : JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);
        Map<String, Object> params = null;
        if (StringUtils.isNotBlank(requestJsonStr)) {
            params = new LinkedHashMap<>();
            JsonNode jsonNodeElements = JacksonUtil.parse(requestJsonStr);
            Iterator<Map.Entry<String, JsonNode>> jsonNodeIterator = jsonNodeElements.fields();
            while (jsonNodeIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = jsonNodeIterator.next();
                JsonNode jsonNode = entry.getValue();
                if (jsonNode.isArray()) {
                    String[] values = new String[jsonNode.size()];
                    for (int i = 0; i < values.length; i++) {
                        String value = jsonNode.get(i).isNull() ? null : String.valueOf(jsonNode.get(i));
                        values[i] = value;
                    }
                    params.put(entry.getKey(), values);
                } else if (!jsonNode.isNull()) {
                    params.put(entry.getKey(), String.valueOf(jsonNode));
                }
            }
        }

        WebTestClient.RequestHeadersSpec requestHeadersSpec = webTestClient.get().uri(url, params);
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestHeadersSpec::header);
        }
        byte[] resultBytes = requestHeadersSpec
                .acceptCharset(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .returnResult()
                .getResponseBody();

        return deserializeResponse(resultBytes, typeReference, url);
    }

}