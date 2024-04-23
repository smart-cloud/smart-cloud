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

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.utility.JacksonUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

class ExceptionControllerAdviceTest extends AbstractTest {

    @Test
    void testBind() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/exception/bind")
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
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.VALIDATE_FAIL);
    }

    @Test
    void testBind2() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/exception/bind2")
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
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.VALIDATE_FAIL);
    }

    @Test
    void testMediaTypeNotSupported() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/exception/mediaTypeNotSupported")
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
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void testMismatchedInputException() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/exception/mismatchedInputException")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("123");

        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn()
                .getResponse();
        mockHttpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String result = new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
        Assertions.assertThat(result).isNotBlank();
        Response response = JacksonUtil.parseObject(result, Response.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.JSON_PARSE_ERROR);
    }

    @Test
    void testOtherException() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/exception/other")
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
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.SERVER_ERROR);
    }

}