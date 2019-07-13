package org.smartframework.cloud.starter.common.support.condition;

import org.smartframework.cloud.starter.common.support.annotation.SmartSpringCloudApplication;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;

/**
 * 判断注解{@link SmartSpringCloudApplication}是否生效
 * 
 * @author liyulin
 * @date 2019-04-27
 */
public class SmartSpringCloudApplicationCondition implements Condition {

	private static String bootstrapClassName = null;

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		ClassMetadata classMetadata = (ClassMetadata) metadata;
		String currentClassName = classMetadata.getClassName();
		// 只有第一个被{@code SmartSpringCloudApplication}标记的类会生效
		if (bootstrapClassName == null) {
			bootstrapClassName = currentClassName;
			return true;
		}

		return bootstrapClassName.equals(currentClassName);
	}

}