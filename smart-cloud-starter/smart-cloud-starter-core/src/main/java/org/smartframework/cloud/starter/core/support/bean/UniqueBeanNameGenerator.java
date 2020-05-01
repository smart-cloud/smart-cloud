package org.smartframework.cloud.starter.core.support.bean;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

/**
 * 生成bean名称的规则
 *
 * @author liyulin
 * @date 2019-04-22
 */
public class UniqueBeanNameGenerator extends AnnotationBeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		if (definition instanceof AnnotatedBeanDefinition) {
			String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
			if (StringUtils.hasText(beanName)) {
				// Explicit bean name found.
				return beanName;
			}
		}

		// 如果该bean名称没有指定，则按照“package+className”的规则生成
			return definition.getBeanClassName();
	}
	
}