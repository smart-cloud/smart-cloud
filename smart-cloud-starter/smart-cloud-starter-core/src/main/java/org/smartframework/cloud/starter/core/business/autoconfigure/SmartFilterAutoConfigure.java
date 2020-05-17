package org.smartframework.cloud.starter.core.business.autoconfigure;

import javax.servlet.Filter;

import org.smartframework.cloud.starter.core.business.security.filter.ReactiveFilter;
import org.smartframework.cloud.starter.core.business.security.filter.ServletFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartFilterAutoConfigure {

	@Bean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public FilterRegistrationBean<Filter> registFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new ServletFilter());
		registration.addUrlPatterns("/*");
		registration.setName(ServletFilter.class.getSimpleName());
		registration.setOrder(1);
		return registration;
	}
	
	@Bean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public ReactiveFilter reactiveFilter() {
		return new ReactiveFilter();
	}

}