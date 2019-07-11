package org.smartframework.cloud.starter.mock.test.unit;

import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.smartframework.cloud.common.pojo.dto.BaseDto;
import org.smartframework.cloud.common.pojo.dto.BasePageReq;
import org.smartframework.cloud.starter.mock.util.MockUtil;
import org.smartframework.cloud.starter.mock.util.TypeReference;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.common.AttributeStrategy;
import uk.co.jemos.podam.common.PodamLongValue;
import uk.co.jemos.podam.common.PodamStrategyValue;
import uk.co.jemos.podam.common.PodamStringValue;

@Slf4j
public class PodamTest extends TestCase {

	/**
	 * 普通非泛型对象
	 */
	public void testMockObject() {
		Product product = MockUtil.mock(Product.class);
		log.info("普通对象head=>{}", product);
		
		Assertions.assertThat(product).isNotNull();
		Assertions.assertThat(product.getName()).isNotBlank();
		Assertions.assertThat(product.getPrice()).isNotNull();
	}

	/**
	 * 单层泛型对象
	 */
	@SuppressWarnings("unchecked")
	public void testMockSimpleGenericObject() {
		// mock方式1
		BasePageReq<Product> resp1 = MockUtil.mock(BasePageReq.class, Product.class);
		log.info("单层泛型对象resp1=>{}", resp1);
		
		Assertions.assertThat(resp1).isNotNull();
		Assertions.assertThat(resp1.getQuery()).isNotNull();
		Assertions.assertThat(resp1.getQuery().getName()).isNotBlank();
		Assertions.assertThat(resp1.getQuery().getPrice()).isNotNull();

		// mock方式2
		BasePageReq<Product> resp2 = MockUtil.mock(new TypeReference<BasePageReq<Product>>() {
		});
		log.info("单层泛型对象resp2=>{}", resp2);

		Assertions.assertThat(resp2).isNotNull();
		Assertions.assertThat(resp2.getQuery()).isNotNull();
		Assertions.assertThat(resp2.getQuery().getName()).isNotBlank();
		Assertions.assertThat(resp2.getQuery().getPrice()).isNotNull();
	}

	public void testMockCustomizeStrategy() {
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
	public class Product extends BaseDto {
		private static final long serialVersionUID = 1L;

		private String name;
		private Long price;
	}

	@Getter
	@Setter
	public class OrderReqBody extends BaseDto {
		private static final long serialVersionUID = 1L;

		@PodamStrategyValue(value = MobileAttributeStrategy.class)
		private String mobile;
		@PodamStringValue(strValue = "小米手机")
		private String name;
		@PodamLongValue(minValue = 100, maxValue = 200)
		private Long price;
		/** 库存 */
		@PodamLongValue(numValue = "50")
		private Long stock;
	}

	/** 手机号码mock生成策略 */
	public static class MobileAttributeStrategy implements AttributeStrategy<String> {

		@Override
		public String getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
			return "13112341234";
		}

	}

}