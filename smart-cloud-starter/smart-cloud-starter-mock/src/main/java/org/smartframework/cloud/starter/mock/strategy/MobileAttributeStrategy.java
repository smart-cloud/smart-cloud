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
package org.smartframework.cloud.starter.mock.strategy;

import java.lang.annotation.Annotation;
import java.util.List;

import org.smartframework.cloud.utility.RandomUtil;

import uk.co.jemos.podam.common.AttributeStrategy;

/**
 * 手机号码mock生成策略
 *
 * @author collin
 * @date 2019-04-17
 */
public class MobileAttributeStrategy implements AttributeStrategy<String> {

	@Override
	public String getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
		return "1" + RandomUtil.generateRangeRandom(3, 8) + RandomUtil.generateRandom(true, 9);
	}

}