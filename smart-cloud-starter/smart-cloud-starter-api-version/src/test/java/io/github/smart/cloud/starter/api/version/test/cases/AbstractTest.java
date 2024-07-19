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
package io.github.smart.cloud.starter.api.version.test.cases;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.smart.cloud.starter.api.version.test.prepare.App;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
@Slf4j
public abstract class AbstractTest {

    protected MockMvc mockMvc;
    @Autowired
    protected ApplicationContext applicationContext;

    @BeforeEach
    public void initMock() {
        // 添加过滤器
        Map<String, Filter> filterMap = applicationContext.getBeansOfType(Filter.class);
        Filter[] filters = new Filter[filterMap.size()];
        int i = 0;
        for (Map.Entry<String, Filter> entry : filterMap.entrySet()) {
            filters[i++] = entry.getValue();
        }

        mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) applicationContext).addFilters(filters).build();
    }

    public <T> T post(String url, Map<String, String> headers, Object req, TypeReference<T> typeReference) throws Exception {
        String requestBody = convertJson(req);
        log.info("test.requestBody={}", requestBody);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(mockHttpServletRequestBuilder::header);
        }
        if (requestBody != null) {
            mockHttpServletRequestBuilder.content(requestBody);
        }

        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        return deserializeResponse(response.getContentAsByteArray(), typeReference);
    }

    /**
     * 序列化响应（主要处理rpc结果）
     *
     * @param resultBytes
     * @param typeReference
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T deserializeResponse(byte[] resultBytes, TypeReference<T> typeReference) throws IOException {
        if (resultBytes == null) {
            log.warn("test.result=null");
            return null;
        }

        String content = new String(resultBytes, StandardCharsets.UTF_8);
        log.info("test.result={}", content);

        if (typeReference.getType() == String.class) {
            return (T) content;
        }

        return JacksonUtil.parseObject(content, typeReference);
    }

    protected String convertJson(Object requestObject) {
        if (requestObject == null) {
            return null;
        }

        if (requestObject instanceof String) {
            return (String) requestObject;
        }

        return JacksonUtil.toJson(requestObject);
    }

}