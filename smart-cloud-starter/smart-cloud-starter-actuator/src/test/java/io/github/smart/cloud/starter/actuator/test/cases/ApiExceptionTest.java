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
package io.github.smart.cloud.starter.actuator.test.cases;

import io.github.smart.cloud.starter.actuator.test.prepare.controller.OrderController;
import org.junit.jupiter.api.Test;

/**
 * controller接口测试
 */
class ApiExceptionTest extends AbstractTest {

    @Test
    void testApiDown() throws Exception {
        OrderController orderController = applicationContext.getBean(OrderController.class);
        for (int i = 1; i <= 6; i++) {
            try {
                orderController.query(i);
            } catch (Exception e) {

            }
        }

        assertActuatorHealth("DOWN", "OrderController#query", 6L, 5L);
    }

    @Test
    void testApiUp() throws Exception {
        OrderController orderController = applicationContext.getBean(OrderController.class);
        for (int i = 1; i <= 6; i++) {
            try {
                orderController.buy();
            } catch (Exception e) {

            }
        }

        assertActuatorHealth("UP", null, null, null);
    }

}