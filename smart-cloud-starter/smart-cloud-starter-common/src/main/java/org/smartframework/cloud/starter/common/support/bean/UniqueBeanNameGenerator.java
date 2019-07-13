package org.smartframework.cloud.starter.common.support.bean;

import org.springframework.beans.factory.BeanFactory;
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

		// 默认的bean名称（不含package，首字母小写）
		String defaultClassName = super.buildDefaultBeanName(definition);
		// 如果该bean名称不存在容器中，则按照className的规则生成；否则按照“package+className”的规则存在。
		BeanFactory beanFactory = (BeanFactory) registry;
		if (beanFactory.containsBean(defaultClassName)) {
			return definition.getBeanClassName();
		} else {
			return defaultClassName;
		}
	}

}