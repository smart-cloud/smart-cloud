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
package org.smartframework.cloud.starter.mybatis.plus.test.cases;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.mybatisplus.MybatisplusApp;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.mybatisplus.biz.ProductInfoOmsBiz;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.mybatisplus.entity.ProductInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisplusApp.class, args = "--spring.profiles.active=mybatisplus")
public class MybatisSqlLogInterceptorTest {

    @Autowired
    private ProductInfoOmsBiz productInfoOmsBiz;

    @BeforeEach
    void cleanData() {
        productInfoOmsBiz.truncate();
    }

    @Test
    void testMybatisLog() {
        ProductInfoEntity entity = productInfoOmsBiz.buildEntity();
        entity.setName("mobile");
        entity.setSellPrice(100L);
        entity.setStock(10L);
        entity.setInsertUser(10L);
        boolean success = productInfoOmsBiz.save(entity);
        Assertions.assertThat(success).isTrue();

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ConsoleAppender appender = ctx.getConfiguration().getAppender("console");
        if(appender==null){
            appender = ctx.getConfiguration().getAppender("Console");
        }
        ByteBuffer byteBuffer = appender.getManager().getByteBuffer().asReadOnlyBuffer();
        String logContent = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        byteBuffer.flip();
        Assertions.assertThat(logContent).containsSubsequence("ProductInfoBaseMapper.insert：", "==>spend：", "==>result==>");
    }

}