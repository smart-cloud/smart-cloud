package org.smartframework.cloud.starter.web.autoconfigure;

import javax.validation.Validator;

import org.smartframework.cloud.starter.web.validation.ValidatorSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Hibernate Validator校验配置
 *
 * @author liyulin
 * @date 2019-03-29
 */
@Configuration
@ConditionalOnProperty(name = "smart.api.validator", havingValue = "true")
public class HibernateValidatorAutoConfigure {

	@Bean
	@ConditionalOnMissingBean
	public MethodValidationPostProcessor methodValidationPostProcessor(final Validator validator) {
		MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
		/** 设置validator模式为快速失败返回 */
		postProcessor.setValidator(ValidatorSingleton.getInstance());
		return postProcessor;
	}

}