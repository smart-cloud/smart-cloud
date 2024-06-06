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
package io.github.smart.cloud.starter.monitor.actuator.test.cases;

import io.github.smart.cloud.starter.monitor.actuator.dto.ApiExceptionDTO;
import io.github.smart.cloud.starter.monitor.actuator.notify.http.ExceptionApiChecker;
import io.github.smart.cloud.starter.monitor.actuator.repository.ApiHealthRepository;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.App;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.controller.OrderController;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.openfeign.IOrderFeign;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, args = "--spring.profiles.active=exception-api-checker", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExceptionApiTest extends AbstractTest {

    @Autowired
    private ExceptionApiChecker exceptionApiChecker;
    @Autowired
    private ApiHealthRepository apiHealthRepository;

    @Test
    void testExceptionApiCheck() throws Exception {
        OrderController orderController = applicationContext.getBean(OrderController.class);
        for (int i = 1; i <= 6; i++) {
            try {
                orderController.query(i);
            } catch (Exception e) {

            }
        }
        for (int i = 95; i <= 110; i++) {
            try {
                orderController.buy(i);
            } catch (Exception e) {

            }
        }

        IOrderFeign orderFeign = applicationContext.getBean(IOrderFeign.class);
        int[] ids = {5, 5, 1};
        for (int id : ids) {
            try {
                orderFeign.query(id);
            } catch (Exception e) {
            }
        }

        // 失败率倒叙测试
        List<ApiExceptionDTO> apiExceptions = apiHealthRepository.getApiExceptions();
        Assertions.assertThat(apiExceptions).hasSize(2);

        ApiExceptionDTO apiException0 = apiExceptions.get(0);
        ApiExceptionDTO apiException1 = apiExceptions.get(1);
        BigDecimal fail0 = BigDecimal.valueOf(apiException0.getFailCount()).divide(BigDecimal.valueOf(apiException0.getTotal()), 10, RoundingMode.HALF_UP);
        BigDecimal fail1 = BigDecimal.valueOf(apiException1.getFailCount()).divide(BigDecimal.valueOf(apiException1.getTotal()), 10, RoundingMode.HALF_UP);
        Assertions.assertThat(fail0).isGreaterThanOrEqualTo(fail1);


        Assertions.assertThat(exceptionApiChecker.checkExceptionApiAndNotice()).isTrue();
    }

    @Test
    void testApiHealthMonitor() {
        ProductService productService = applicationContext.getBean(ProductService.class);
        int[] ids = {5, 5, 1};
        for (int id : ids) {
            try {
                productService.query(id);
            } catch (Exception e) {
            }
        }


        List<ApiExceptionDTO> apiExceptions = apiHealthRepository.getApiExceptions();
        Assertions.assertThat(apiExceptions).hasSize(1);
    }

}