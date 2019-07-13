package org.smartframework.cloud.starter.mock.strategy;

import java.lang.annotation.Annotation;
import java.util.List;

import org.smartframework.cloud.utility.RandomUtil;

import uk.co.jemos.podam.common.AttributeStrategy;

/**
 * 手机号码mock生成策略
 *
 * @author liyulin
 * @date 2019-04-17
 */
public class MobileAttributeStrategy implements AttributeStrategy<String> {

	@Override
	public String getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
		return "1" + RandomUtil.generateRangeRandom(3, 8) + RandomUtil.generateRandom(true, 9);
	}

}