package org.smartframework.cloud.starter.test.core;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.smartframework.cloud.starter.test.AbstractUnitTest;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * webmvc集成测试父类
 *
 * @author liyulin
 * @date 2020-09-07
 */
@Slf4j
public class WebMvcIntegrationTest extends AbstractUnitTest implements IIntegrationTest {

    @Autowired
    protected WebApplicationContext applicationContext;
    protected MockMvc mockMvc = null;

    @Before
    public void initMock() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        }
    }


    /**
     * post请求
     *
     * @param url           请求mapping的地址
     * @param req           请求参数
     * @param typeReference 返回对象类型
     * @return
     * @throws Exception
     */
    @Override
    public <T> T post(String url, Object req, TypeReference<T> typeReference) throws Exception {
        String requestBody = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestBody);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        if (requestBody != null) {
            mockHttpServletRequestBuilder.content(requestBody);
        }

        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String content = response.getContentAsString();
        log.info("test.result={}", content);

        return JacksonUtil.parseObject(content, typeReference);
    }

    /**
     * get请求
     *
     * @param url           请求mapping的地址
     * @param req           请求参数
     * @param typeReference 返回对象类型
     * @return
     * @throws Exception
     */
    @Override
    public <T> T get(String url, Object req, TypeReference<T> typeReference)
            throws Exception {
        String requestJsonStr = JacksonUtil.toJson(req);
        log.info("test.requestBody={}", requestJsonStr);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(url)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> requestMap = JacksonUtil.parseObject(requestJsonStr, new TypeReference<Map<String, String>>() {
        });
        if (requestMap != null) {
            requestMap.forEach(mockHttpServletRequestBuilder::param);
        }

        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String content = response.getContentAsString();

        log.info("test.result={}", content);

        return JacksonUtil.parseObject(content, typeReference);
    }

}