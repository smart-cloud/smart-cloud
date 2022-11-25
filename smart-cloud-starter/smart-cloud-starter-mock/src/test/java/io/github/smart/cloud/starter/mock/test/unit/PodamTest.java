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
package io.github.smart.cloud.starter.mock.test.unit;

import io.github.smart.cloud.starter.mock.strategy.MobileAttributeStrategy;
import io.github.smart.cloud.starter.mock.strategy.MoneyAttributeStrategy;
import io.github.smart.cloud.starter.mock.util.MockUtil;
import io.github.smart.cloud.starter.mock.util.TypeReference;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.common.PodamLongValue;
import uk.co.jemos.podam.common.PodamStrategyValue;
import uk.co.jemos.podam.common.PodamStringValue;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Slf4j
class PodamTest {

    /**
     * 普通非泛型对象
     */
    @Test
    void testMockObject() {
        Product product = MockUtil.mock(Product.class);
        log.info("普通对象head=>{}", product);

        Assertions.assertThat(product).isNotNull();
        Assertions.assertThat(product.getName()).isNotBlank();
        Assertions.assertThat(product.getPrice()).isNotNull();
    }

    /**
     * 单层泛型对象
     */
    @Test
    void testMockSimpleGenericObject() {
        // mock方式1
        List<Product> list = MockUtil.mock(List.class, Product.class);
        log.info("单层泛型对象resp1=>{}", JacksonUtil.toJson(list));

        Assertions.assertThat(list).isNotEmpty();

        // mock方式2
        Set<Product> set = MockUtil.mock(new TypeReference<Set<Product>>() {
        });
        log.info("单层泛型对象resp2=>{}", JacksonUtil.toJson(set));

        Assertions.assertThat(set).isNotEmpty();

        Product product = MockUtil.mock(new TypeReference<Product>() {
        });
        Assertions.assertThat(product).isNotNull();
    }

    @Test
    void testMockCustomizeStrategy() {
        OrderReqBody orderReqBody = MockUtil.mock(OrderReqBody.class);
        log.info("自定义mock策略orderReqBody=>{}", orderReqBody);

        Assertions.assertThat(orderReqBody).isNotNull();
        Assertions.assertThat(orderReqBody.getMobile().length()).isEqualTo(11);
        Assertions.assertThat(StringUtils.isNumeric(orderReqBody.getMobile())).isTrue();
        Assertions.assertThat(orderReqBody.getName()).isEqualTo("小米手机");
        Assertions.assertThat(orderReqBody.getPrice()).isBetween(100L, 200L);
        Assertions.assertThat(orderReqBody.getTotalMoney()).isBetween(100L, 1000000L);
        Assertions.assertThat(orderReqBody.getStock()).isEqualTo(50L);
    }

    @Getter
    @Setter
    @ToString
    class Product implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private Long price;
    }

    @Getter
    @Setter
    @ToString
    class OrderReqBody implements Serializable {
        private static final long serialVersionUID = 1L;

        @PodamStrategyValue(value = MobileAttributeStrategy.class)
        private String mobile;
        @PodamStringValue(strValue = "小米手机")
        private String name;
        @PodamLongValue(minValue = 100, maxValue = 200)
        private Long price;
        @PodamStrategyValue(value = MoneyAttributeStrategy.class)
        private Long totalMoney;
        @PodamLongValue(numValue = "50")
        private Long stock;
    }

}