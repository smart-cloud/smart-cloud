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
package io.github.smart.cloud.starter.rate.limit.test.cases;

import io.github.smart.cloud.exception.AccessFrequentlyException;
import io.github.smart.cloud.starter.rate.limit.RateLimitInstanceFactory;
import io.github.smart.cloud.starter.rate.limit.properties.RateLimitProperties;
import io.github.smart.cloud.starter.rate.limit.test.prepare.Application;
import io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class RateLimitTest {

    @Autowired
    private ProductController productController;
    @Autowired
    private RateLimitInstanceFactory rateLimitInstanceFactory;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RefreshScope refreshScope;

    @Test
    void testPropertiesRule() throws Exception {
        Semaphore querySemaphore = rateLimitInstanceFactory.get("io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController.query");
        Assertions.assertThat(querySemaphore).isNotNull();
        Assertions.assertThat(querySemaphore.availablePermits()).isEqualTo(20);

        Semaphore createSemaphore = rateLimitInstanceFactory.get("io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController.create");
        Assertions.assertThat(createSemaphore).isNotNull();
        Assertions.assertThat(createSemaphore.availablePermits()).isEqualTo(1);
    }

    @Test
    void testRateLimitAnnotationRule() throws Exception {
        AtomicReference<Exception> exceptionAtomicReference1 = new AtomicReference<>();
        AtomicReference<Exception> exceptionAtomicReference2 = new AtomicReference<>();
        new Thread(() -> {
            try {
                productController.create();
            } catch (Exception e) {
                exceptionAtomicReference1.set(e);
            }
        }).start();
        new Thread(() -> {
            try {
                productController.create();
            } catch (Exception e) {
                exceptionAtomicReference2.set(e);
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(1200);

        boolean onlyOneException = (exceptionAtomicReference1.get() != null && exceptionAtomicReference2.get() == null)
                || (exceptionAtomicReference1.get() == null && exceptionAtomicReference2.get() != null);
        Assertions.assertThat(onlyOneException).isTrue();

        boolean isInstanceOfAccessFrequentlyException = (exceptionAtomicReference1.get() != null && exceptionAtomicReference1.get() instanceof AccessFrequentlyException)
                || (exceptionAtomicReference2.get() != null && exceptionAtomicReference2.get() instanceof AccessFrequentlyException);
        Assertions.assertThat(isInstanceOfAccessFrequentlyException).isTrue();
    }

    /**
     * 测试配置中心动态刷新场景
     */
    @Test
    void testDynamicConfig() {
        RateLimitProperties rateLimitProperties = applicationContext.getBean(RateLimitProperties.class);

        Map<String, Integer> config = rateLimitProperties.getConfig();
        config.put("io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController.update", 3);
        config.put("io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController.query", 100);
        refreshScope.refresh("scopedTarget.rateLimitInstanceFactory");

        Semaphore updateSemaphore = rateLimitInstanceFactory.get("io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController.update");
        Assertions.assertThat(updateSemaphore).isNotNull();
        Assertions.assertThat(updateSemaphore.availablePermits()).isEqualTo(3);


        Semaphore querySemaphore = rateLimitInstanceFactory.get("io.github.smart.cloud.starter.rate.limit.test.prepare.controller.ProductController.query");
        Assertions.assertThat(querySemaphore).isNotNull();
        Assertions.assertThat(querySemaphore.availablePermits()).isEqualTo(100);
    }

}