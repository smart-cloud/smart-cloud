package org.smartframework.cloud.starter.swagger.autoconfigure;

import org.smartframework.cloud.starter.swagger.validators.plugins.parameter.ExpandedParameterNotBlankAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.parameter.ExpandedParameterNotEmptyAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.schema.NotBlankAnnotationPlugin;
import org.smartframework.cloud.starter.swagger.validators.plugins.schema.NotEmptyAnnotationPlugin;
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
	public ExpandedParameterNotBlankAnnotationPlugin expanderNotBlank() {
		return new ExpandedParameterNotBlankAnnotationPlugin();
	}

	@Bean
	public org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotBlankAnnotationPlugin parameterNotBlank() {
		return new org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotBlankAnnotationPlugin();
	}

	@Bean
	public NotBlankAnnotationPlugin notBlankPlugin() {
		return new NotBlankAnnotationPlugin();
	}
	// ------@NotBlank end

	// ------@Empty start
	@Bean
	public ExpandedParameterNotEmptyAnnotationPlugin expanderNotEmpty() {
		return new ExpandedParameterNotEmptyAnnotationPlugin();
	}

	@Bean
	public org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotEmptyAnnotationPlugin parameterNotEmpty() {
		return new org.smartframework.cloud.starter.swagger.validators.plugins.parameter.NotEmptyAnnotationPlugin();
	}

	@Bean
	public NotEmptyAnnotationPlugin notEmptyPlugin() {
		return new NotEmptyAnnotationPlugin();
	}
	// ------@Empty end

}