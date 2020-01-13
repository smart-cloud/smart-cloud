package org.smartframework.cloud.starter.swagger.autoconfigure;

import org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotBlankExpandedParameterAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotBlankParameterAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotEmptyExpandedParameterAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotEmptyParameterAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.schema.NotBlankModelPropertyAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.schema.NotEmptyModelPropertyAnnotationPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;

/**
 * 校验注解文档支持配置
 * 
 * @author liyulin
 * @date 2019-07-18
 */
@Configuration
public class SmartBeanValidatorPluginsConfiguration extends BeanValidatorPluginsConfiguration {

	// ------@NotBlank start
	@Bean
	public NotBlankExpandedParameterAnnotationPlugin expanderNotBlank() {
		return new NotBlankExpandedParameterAnnotationPlugin();
	}

	@Bean
	public NotBlankParameterAnnotationPlugin parameterNotBlank() {
		return new NotBlankParameterAnnotationPlugin();
	}

	@Bean
	public NotBlankModelPropertyAnnotationPlugin notBlankPlugin() {
		return new NotBlankModelPropertyAnnotationPlugin();
	}
	// ------@NotBlank end

	// ------@Empty start
	@Bean
	public NotEmptyExpandedParameterAnnotationPlugin expanderNotEmpty() {
		return new NotEmptyExpandedParameterAnnotationPlugin();
	}

	@Bean
	public NotEmptyParameterAnnotationPlugin parameterNotEmpty() {
		return new NotEmptyParameterAnnotationPlugin();
	}

	@Bean
	public NotEmptyModelPropertyAnnotationPlugin notEmptyPlugin() {
		return new NotEmptyModelPropertyAnnotationPlugin();
	}
	// ------@Empty end

}