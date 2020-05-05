package org.smartframework.cloud.starter.core.business.autoconfigure;

import javax.servlet.Filter;

import org.smartframework.cloud.starter.core.business.security.filter.SmartHttpFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Filter.class)
public class SmartFilterAutoConfigure {

	@Bean
	public FilterRegistrationBean<Filter> registFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new SmartHttpFilter());
		registration.addUrlPatterns("/*");
		registration.setName(SmartHttpFilter.class.getSimpleName());
		registration.setOrder(1);
		return registration;
	}

}