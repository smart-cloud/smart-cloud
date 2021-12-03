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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.starter.web.test.prepare.Application;
import org.smartframework.cloud.starter.web.test.prepare.controller.ProductController;
import org.smartframework.cloud.starter.web.test.prepare.vo.ProductCreateReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ServletApiLogInterceptorTest {

    @Autowired
    private ProductController productController;

    @Test
    void testQuery() throws IOException {
        Response<String> response = productController.query(100L);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());
        Assertions.assertThat(response.getBody()).isNotBlank();

        checkLog();
    }


    @Test
    void testCreate() throws IOException {
        ProductCreateReqVO reqVO = new ProductCreateReqVO();
        reqVO.setDesc("手机");
        reqVO.setName("iphone");
        Response<Boolean> response = productController.create(reqVO);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());
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
        Assertions.assertThat(StringUtils.containsAny(logContent, "api.info=>", "api.slow=>", "api.error=>")).isTrue();
    }

}