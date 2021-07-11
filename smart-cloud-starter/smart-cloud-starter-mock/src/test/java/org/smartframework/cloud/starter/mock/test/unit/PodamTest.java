package org.smartframework.cloud.starter.mock.test.unit;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.starter.mock.util.MockUtil;
import org.smartframework.cloud.starter.mock.util.TypeReference;
import org.smartframework.cloud.utility.JacksonUtil;
import uk.co.jemos.podam.common.AttributeStrategy;
import uk.co.jemos.podam.common.PodamLongValue;
import uk.co.jemos.podam.common.PodamStrategyValue;
import uk.co.jemos.podam.common.PodamStringValue;

import java.lang.annotation.Annotation;
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
        Assertions.assertThat(orderReqBody.getStock()).isEqualTo(50L);
    }

    @Getter
    @Setter
    class Product extends Base {
        private static final long serialVersionUID = 1L;

        private String name;
        private Long price;
    }

    @Getter
    @Setter
    class OrderReqBody extends Base {
        private static final long serialVersionUID = 1L;

        @PodamStrategyValue(value = MobileAttributeStrategy.class)
        private String mobile;
        @PodamStringValue(strValue = "小米手机")
        private String name;
        @PodamLongValue(minValue = 100, maxValue = 200)
        private Long price;
        /**
         * 库存
         */
        @PodamLongValue(numValue = "50")
        private Long stock;
    }

    /**
     * 手机号码mock生成策略
     */
    static class MobileAttributeStrategy implements AttributeStrategy<String> {

        @Override
        public String getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
            return "13112341234";
        }

    }

}