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
package io.github.smart.cloud.starter.actuator.test.cases;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.smart.cloud.starter.actuator.repository.ApiHealthRepository;
import io.github.smart.cloud.starter.actuator.test.prepare.App;
import io.github.smart.cloud.starter.actuator.test.util.JacksonUtil;
import org.assertj.core.api.Assertions;
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
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractTest {

    protected MockMvc mockMvc = null;
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

        ApiHealthRepository apiHealthRepository = applicationContext.getBean(ApiHealthRepository.class);
        apiHealthRepository.clearApiStatusStatistics();
    }

    /**
     * 校验actuator/health接口
     *
     * @param status
     * @param apiName
     * @param totalCount
     * @param failCount
     * @throws Exception
     */
    protected void assertActuatorHealth(String status, String apiName, Long totalCount, Long failCount) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/actuator/health")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        mockHttpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String result = new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
        Assertions.assertThat(result).isNotBlank();

        JsonNode jsonNode = JacksonUtil.parse(result);
        Assertions.assertThat(jsonNode).isNotNull();
        Assertions.assertThat(jsonNode.get("status").asText()).isEqualTo(status);
        if (!"DOWN".equals(status)) {
            return;
        }

        JsonNode componentsNode = jsonNode.get("components");
        Assertions.assertThat(componentsNode).isNotNull();

        JsonNode apiNode = componentsNode.get("api");
        Assertions.assertThat(apiNode).isNotNull();
        Assertions.assertThat(apiNode.get("status").asText()).isEqualTo("DOWN");

        JsonNode apiDetailsNode = apiNode.get("details");
        Assertions.assertThat(apiDetailsNode).isNotNull();

        JsonNode unHealthInfosNode = apiDetailsNode.get("unHealthInfos");
        Assertions.assertThat(unHealthInfosNode).isNotNull();
        Assertions.assertThat(unHealthInfosNode.size()).isGreaterThanOrEqualTo(1);

        JsonNode node = unHealthInfosNode.get(0);
        Assertions.assertThat(node.get("name").asText()).isEqualTo(apiName);
        Assertions.assertThat(node.get("total").asLong()).isEqualTo(totalCount);
        Assertions.assertThat(node.get("failCount").asLong()).isEqualTo(failCount);
    }

}