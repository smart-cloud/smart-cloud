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
import io.github.smart.cloud.starter.rate.limit.test.prepare.Application;
import io.github.smart.cloud.starter.rate.limit.test.prepare.service.IProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class RateLimitTest {

    @Autowired
    private IProductService productService;

    @Test
    void test() throws Exception {
        AtomicReference<Exception> exceptionAtomicReference1 = new AtomicReference<>();
        AtomicReference<Exception> exceptionAtomicReference2 = new AtomicReference<>();
        new Thread(() -> {
            try {
                productService.create();
            } catch (Exception e) {
                exceptionAtomicReference1.set(e);
            }
        }).start();
        new Thread(() -> {
            try {
                productService.create();
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

}