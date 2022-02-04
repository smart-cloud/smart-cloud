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
package org.smartframework.cloud.starter.locale.test.cases;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.constants.CommonReturnCodes;
import org.smartframework.cloud.starter.locale.test.prepare.Application;
import org.smartframework.cloud.starter.locale.test.prepare.controller.OrderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class LocaleInterceptorTest {

    @Autowired
    private OrderController orderController;

    @Test
    void testSubmit() {
        Response<String> response = orderController.submit();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS);
        Assertions.assertThat(StringUtils.containsAny(response.getHead().getMessage(), "Success", "成功")).isTrue();
    }

}