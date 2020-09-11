package org.smartframework.cloud.starter.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
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
    public WebTestClient webTestClient;

    @Override
    public <T> T post(String url, Object req, TypeReference<T> typeReference) throws Exception {
        String requestJsonStr = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);
        byte[] resultBytes = webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
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

    @Override
    public <T> T get(String url, Object req, TypeReference<T> typeReference) throws Exception {
        String requestJsonStr = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);
        Map<String, String> params = JacksonUtil.parseObject(requestJsonStr, new TypeReference<Map<String, String>>() {
        });
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