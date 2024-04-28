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

import io.github.smart.cloud.starter.actuator.test.prepare.openfeign.IOrderFeign;
import org.junit.jupiter.api.Test;

/**
 * openfeign接口测试
 */
class FeignApiExceptionTest extends AbstractActuatorHealthTest {

    @Test
    void testFeignApi() throws Exception {
        IOrderFeign orderFeign = applicationContext.getBean(IOrderFeign.class);
        int[] ids = {5, 5, 1};
        for (int id : ids) {
            try {
                orderFeign.query(id);
            } catch (Exception e) {
            }
        }

        assertActuatorHealth("DOWN", "IOrderFeign#query", 3L, 1L);
    }

}