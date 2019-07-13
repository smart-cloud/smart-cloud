package org.smartframework.cloud.starter.mock.strategy;

import java.lang.annotation.Annotation;
import java.util.List;

import org.smartframework.cloud.utility.RandomUtil;

import uk.co.jemos.podam.common.AttributeStrategy;

/**
 * 金额mock生成策略
 *
 * @author liyulin
 * @date 2019-04-17
 */
public class MoneyAttributeStrategy implements AttributeStrategy<Long> {

	@Override
	public Long getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
		return (long) RandomUtil.generateRangeRandom(100, 1000000);
	}

}