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

import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.starter.configure.test.prepare.Application;
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
        Assertions.assertThat(beanOfType).isNotEmpty();
        Assertions.assertThat(beanOfType.size()).isEqualTo(1);

        // check properties values
        SmartProperties smartProperties = applicationContext.getBean(SmartProperties.class);
        Assertions.assertThat(smartProperties.isAsync()).isFalse();

        // log
        Assertions.assertThat(smartProperties.getLog()).isNotNull();
        Assertions.assertThat(smartProperties.getLog().isRpclog()).isFalse();
        Assertions.assertThat(smartProperties.getLog().isApilog()).isFalse();
        Assertions.assertThat(smartProperties.getLog().getSlowApiMinCost()).isEqualTo(4000);

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
        Assertions.assertThat(smartProperties.getMock().getWhilelist()).isNotEmpty();
        Assertions.assertThat(smartProperties.getMock().getWhilelist().size()).isEqualTo(2);
        Assertions.assertThat(smartProperties.getMock().getWhilelist()).contains("xxxx.query", "yyyy.submit");
    }

}