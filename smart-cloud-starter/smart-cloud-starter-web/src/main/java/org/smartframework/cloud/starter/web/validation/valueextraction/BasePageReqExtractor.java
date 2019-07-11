package org.smartframework.cloud.starter.web.validation.valueextraction;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.validator.internal.engine.valueextraction.ValueExtractorDescriptor;
import org.smartframework.cloud.common.pojo.dto.BasePageReq;

/**
 * <code>BasePageReq</code>泛型参数T校验生效
 *
 * @author liyulin
 * @date 2019年3月29日下午11:13:23
 */
public class BasePageReqExtractor implements ValueExtractor<BasePageReq<@ExtractedValue ?>> {

	public static final ValueExtractorDescriptor DESCRIPTOR = new ValueExtractorDescriptor(new BasePageReqExtractor());

	private BasePageReqExtractor() {
	}

	@Override
	public void extractValues(BasePageReq<?> originalValue, ValueReceiver receiver) {
		receiver.value(null, originalValue.getQuery());
	}

}