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
package io.github.smart.cloud.test.core.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.smart.cloud.api.core.user.context.AbstractUserContext;
import io.github.smart.cloud.api.core.user.context.SmartUser;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

/**
 * SpringBoot集成测试基类
 *
 * @author collin
 * @date 2019-04-22
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class AbstractIntegrationTest {

    static {
        String closeValue = Boolean.FALSE.toString();
        // 单元测试环境下，关闭依赖
        // 1.关闭api元数据上传
        System.setProperty("smart.uploadApiMeta", closeValue);

        // 2.关闭nacos
        System.setProperty("spring.cloud.nacos.discovery.enabled", closeValue);
        System.setProperty("spring.cloud.nacos.config.enabled", closeValue);

        // 3.单元测试环境下，关闭Sentinel自动化配置
        System.setProperty("spring.cloud.sentinel.enabled", closeValue);
    }

    @Autowired
    protected ApplicationContext applicationContext;

    @BeforeEach
    public void beforeTestMethod() {
        fillMockUserToContext();
    }

    /**
     * 填充当前线程上下文需要的用户信息
     */
    private static void fillMockUserToContext() {
        SmartUser smartUserMock = new SmartUser();
        smartUserMock.setId(1L);
        smartUserMock.setUsername("collin");
        smartUserMock.setRealName("Collin.Lee");
        smartUserMock.setMobile("13112341234");
        AbstractUserContext.setContext(smartUserMock);
    }

    /**
     * 序列化响应（主要处理rpc结果）
     *
     * @param resultBytes
     * @param typeReference
     * @param url
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T deserializeResponse(byte[] resultBytes, TypeReference<T> typeReference, String url) throws IOException {
        if (resultBytes == null) {
            log.warn("test.result=null");
            return null;
        }

        String content = new String(resultBytes);
        log.info("test.result={}", content);

        return JacksonUtil.parseObject(content, typeReference);
    }

}