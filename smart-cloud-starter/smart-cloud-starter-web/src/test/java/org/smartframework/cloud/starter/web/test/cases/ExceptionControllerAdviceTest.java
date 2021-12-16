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
package org.smartframework.cloud.starter.web.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.starter.web.test.prepare.Application;
import org.smartframework.cloud.utility.JacksonUtil;
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
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ExceptionControllerAdviceTest {

    private MockMvc mockMvc = null;
    ;
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

    @Test
    void testNpe() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/npe")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        mockHttpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String result = new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
        Assertions.assertThat(result).isNotBlank();
        Response response = JacksonUtil.parseObject(result, Response.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.VALIDATE_FAIL.getCode());
    }

}