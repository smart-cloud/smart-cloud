package org.smartframework.cloud.starter.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * web reactive集成测试父类
 *
 * @author liyulin
 * @date 2020-09-07
 */
@Slf4j
@AutoConfigureWebTestClient
public class WebReactiveIntegrationTest extends AbstractIntegrationTest implements IIntegrationTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Override
    public <T> T post(String url, Object req, TypeReference<T> typeReference) throws Exception {
        String requestJsonStr = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);
        WebTestClient.RequestBodySpec requestBodySpec = webTestClient.post().uri(url)
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

        String result = new String(resultBytes);
        log.info("test.result={}", result);

        return JacksonUtil.parseObject(result, typeReference);
    }

    @Override
    public <T> T get(String url, Object req, TypeReference<T> typeReference) throws Exception {
        String requestJsonStr = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);
        Map<String, Object> params = null;
        if (StringUtils.isNotBlank(requestJsonStr)) {
            params = new LinkedHashMap<>();
            JsonNode jsonNodeElements = JacksonUtil.parseObject(requestJsonStr);
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


        byte[] resultBytes = webTestClient.get().uri(url, params)
                .acceptCharset(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .returnResult()
                .getResponseBody();
        String result = new String(resultBytes);
        log.info("test.result={}", result);

        return JacksonUtil.parseObject(result, typeReference);
    }

}