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
package io.github.smart.cloud.starter.web.test.cases;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.starter.web.test.prepare.controller.ProductController;
import io.github.smart.cloud.starter.web.test.prepare.vo.ProductCreateReqVO;
import io.github.smart.cloud.utility.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class ServletApiLogInterceptorTest extends AbstractTest {

    @Autowired
    private ProductController productController;

    @Test
    void testQuery() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/product?id=100")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        mockHttpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String result = new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
        Assertions.assertThat(result).isNotBlank();
        Response<String> response = JacksonUtil.parseObject(result, new TypeReference<Response>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.SUCCESS);
        Assertions.assertThat(response.getBody()).isNotBlank();
    }


    @Test
    void testCreate() throws IOException {
        ProductCreateReqVO reqVO = new ProductCreateReqVO();
        reqVO.setDesc("手机");
        reqVO.setName("iphone");
        Response<Boolean> response = productController.create(reqVO);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.SUCCESS);
        Assertions.assertThat(response.getBody()).isTrue();

        checkLog();
    }

    /**
     * 日志包含切面日志
     *
     * @throws IOException
     */
    private void checkLog() throws IOException {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String appenderName = "console";
        ConsoleAppender appender = ctx.getConfiguration().getAppender(appenderName);
        ByteBuffer byteBuffer = appender.getManager().getByteBuffer().asReadOnlyBuffer();
        String logContent = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        byteBuffer.flip();
        Assertions.assertThat(StringUtils.containsAny(logContent, "api.log=>", "api.slow=>", "api.error=>")).isTrue();
    }

}