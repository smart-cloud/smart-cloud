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
package org.smartframework.cloud.starter.mock.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.starter.mock.test.prepare.TestApplication;
import org.smartframework.cloud.starter.mock.test.prepare.service.StockService;
import org.smartframework.cloud.starter.mock.test.prepare.vo.DeductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
class MethodMockTest {

    @Autowired
    private StockService stockService;

    @Test
    void testDeduct() {
        DeductDTO deductDTO = stockService.deduct();
        Assertions.assertThat(deductDTO).isNotNull();
        Assertions.assertThat(deductDTO.getResult()).isFalse();
    }

    @Test
    void testList() {
        Response<List<DeductDTO>> response = stockService.list();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());
        Assertions.assertThat(response.getBody()).isNotEmpty();
    }

    /**
     * 白名单测试
     */
    @Test
    void testQuery() {
        DeductDTO deductDTO = stockService.query();
        Assertions.assertThat(deductDTO).isNotNull();
        Assertions.assertThat(deductDTO.getResult()).isTrue();
    }

}