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
package io.github.smart.cloud.starter.rpc.feign.condition;

import java.util.Set;

import io.github.smart.cloud.starter.core.business.util.ReflectionUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <code>FeignClient</code>生效条件判断
 * 
 * <h3>判断逻辑</h3>
 * <ul>
 * <li>1、获取使用{@link FeignClient}的interface的Class；
 * <li>2、通过反射，获取interface对应的所有实现类；
 * <li>3、遍历interface对应的所有实现类，判断是否存在一个Class被{@link Controller}或{@link RestController}注解修饰（排除interface对应的熔断bean）；
 * <li>4、如果存在，则{@link #matches(ConditionContext, AnnotatedTypeMetadata)}返回false，{@code FeignClient}不生效；否则，返回true，{@code FeignClient}生效。
 * </ul>
 *
 * @author collin
 * @date 2019-03-22
 */
@Slf4j
public class SmartFeignClientCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// 1、获取使用@FeignClient的interface的Class
		ClassMetadata classMetadata = (ClassMetadata) metadata;
		String interfaceClassName = classMetadata.getClassName();
		Class<?> interfaceClass = null;
		try {
			interfaceClass = Class.forName(interfaceClassName);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			return false;
		}

		// 2、获取interface对应的所有实现类
		Set<?> subTypes = ReflectionUtil.getSubTypesOf(interfaceClass);

		// 3、判断是否存在RPC interface的实现类，且实现类上有Controller、RestController注解
		if (subTypes == null || subTypes.isEmpty()) {
			return true;
		}
		// 遍历bean
		for (Object subType : subTypes) {
			Class<?> subTypeClass = (Class<?>) subType;
			boolean isRpcImplementClass = isRpcImplementClass(subTypeClass);
			if (isRpcImplementClass) {
				return false;
			}
		}

		return true;
	}

	private boolean isRpcImplementClass(Class<?> clazz) {
		return (AnnotationUtils.findAnnotation(clazz, Controller.class) != null)
				|| (AnnotationUtils.findAnnotation(clazz, RestController.class) != null);
	}

}