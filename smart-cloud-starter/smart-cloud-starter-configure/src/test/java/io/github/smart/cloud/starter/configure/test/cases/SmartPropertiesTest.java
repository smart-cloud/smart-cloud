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
package io.github.smart.cloud.starter.configure.test.cases;

import io.github.smart.cloud.constants.LogLevel;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.configure.test.prepare.Application;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class SmartPropertiesTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void test() {
        // bean not null check
        Map<String, SmartProperties> beanOfType = applicationContext.getBeansOfType(SmartProperties.class);
        Assertions.assertThat(beanOfType)
                .isNotEmpty()
                .hasSize(1);

        // check properties values
        SmartProperties smartProperties = applicationContext.getBean(SmartProperties.class);
        Assertions.assertThat(smartProperties.getAsync()).isNotNull();
        Assertions.assertThat(smartProperties.getAsync().isEnable()).isTrue();
        Assertions.assertThat(smartProperties.getAsync().getCorePoolSize()).isGreaterThan(0);
        Assertions.assertThat(smartProperties.getAsync().getMaxPoolSize()).isGreaterThan(0);
        Assertions.assertThat(smartProperties.getAsync().getKeepAliveSeconds()).isEqualTo(60);
        Assertions.assertThat(smartProperties.getAsync().getQueueCapacity()).isEqualTo(512);
        Assertions.assertThat(smartProperties.getAsync().getAwaitTerminationSeconds()).isEqualTo(0);

        // log
        Assertions.assertThat(smartProperties.getApiLog()).isNotNull();
        Assertions.assertThat(smartProperties.getApiLog().isEnable()).isTrue();
        Assertions.assertThat(smartProperties.getApiLog().getLevel()).isEqualTo(LogLevel.INFO);
        Assertions.assertThat(smartProperties.getApiLog().getSlowApiMinCost()).isEqualTo(4000);

        Assertions.assertThat(smartProperties.getFeign()).isNotNull();
        Assertions.assertThat(smartProperties.getFeign().getLog()).isNotNull();
        Assertions.assertThat(smartProperties.getFeign().getLog().isEnable()).isTrue();
        Assertions.assertThat(smartProperties.getFeign().getLog().getLevel()).isEqualTo(LogLevel.WARN);
        Assertions.assertThat(smartProperties.getFeign().getLog().getSlowApiMinCost()).isEqualTo(2500);
        Assertions.assertThat(smartProperties.getFeign().getTransferHeaderNames()).contains("smart-user");

        Assertions.assertThat(smartProperties.getMethodLog()).isNotNull();
        Assertions.assertThat(smartProperties.getMethodLog().isEnable()).isTrue();
        Assertions.assertThat(smartProperties.getMethodLog().getLevel()).isEqualTo(LogLevel.DEBUG);
        Assertions.assertThat(smartProperties.getMethodLog().getSlowApiMinCost()).isEqualTo(3100);

        // xxl-job
        Assertions.assertThat(smartProperties.getXxlJob()).isNotNull();
        Assertions.assertThat(smartProperties.getXxlJob().getAdminAddresses()).isEqualTo("192.168.1.15");
        Assertions.assertThat(smartProperties.getXxlJob().getAppName()).isEqualTo("yyy");
        Assertions.assertThat(smartProperties.getXxlJob().getIp()).isEqualTo("127.0.0.1");
        Assertions.assertThat(smartProperties.getXxlJob().getPort()).isEqualTo(80);
        Assertions.assertThat(smartProperties.getXxlJob().getAccessToken()).isEqualTo("xxxx");
        Assertions.assertThat(smartProperties.getXxlJob().getLogPath()).isEqualTo("/log");
        Assertions.assertThat(smartProperties.getXxlJob().getLogRetentionDays()).isEqualTo(10);

        // locale
        Assertions.assertThat(smartProperties.getLocale()).isNotNull();
        Assertions.assertThat(smartProperties.getLocale().getEncoding()).isEqualTo("GBK");
        Assertions.assertThat(smartProperties.getLocale().getCacheSeconds()).isEqualTo(3600);

        // mock
        Assertions.assertThat(smartProperties.getMock()).isNotNull();
        Assertions.assertThat(smartProperties.getMock().isApi()).isTrue();
        Assertions.assertThat(smartProperties.getMock().isMethod()).isTrue();
        Assertions.assertThat(smartProperties.getMock().getWhilelist()).isNotEmpty().hasSize(2);
        Assertions.assertThat(smartProperties.getMock().getWhilelist()).contains("xxxx.query", "yyyy.submit");

        // mybatis
        Assertions.assertThat(smartProperties.getMybatis()).isNotNull();
        Assertions.assertThat(smartProperties.getMybatis().isEnable()).isTrue();
        Assertions.assertThat(smartProperties.getMybatis().getLogLevel()).isEqualTo("warn");
        Assertions.assertThat(smartProperties.getMybatis().getCryptKey()).isEqualTo("123456");

        // rabbitmq
        Assertions.assertThat(smartProperties.getRabbitmq()).isNotNull();
        Assertions.assertThat(smartProperties.getRabbitmq().getLevel()).isEqualTo("info");
    }

}