package io.github.smart.cloud.starter.monitor.actuator.test.cases;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.App;
import io.github.smart.cloud.starter.monitor.actuator.test.util.JacksonUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractActuatorHealthTest extends AbstractTest {

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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/actuator/health").characterEncoding(StandardCharsets.UTF_8.name()).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);

        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(mockHttpServletRequestBuilder).andReturn().getResponse();
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