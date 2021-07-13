package org.smartframework.cloud.api.core.user.context.autoconfigure;

import org.smartframework.cloud.api.core.user.context.filter.CleanUserContextServletFilter;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnClass(name = {"javax.servlet.Filter"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CleanUserContextServletFilterAutoConfiguration {

    @Bean
    public CleanUserContextServletFilter cleanUserContextServletFilter() {
        return new CleanUserContextServletFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> registCleanUserContextServletFilter(final CleanUserContextServletFilter cleanUserContextServletFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(cleanUserContextServletFilter);
        registration.addUrlPatterns("/*");
        registration.setName(CleanUserContextServletFilter.class.getSimpleName());
        registration.setOrder(OrderConstant.CLEAN_USER_CONTEXT_FILTER);
        return registration;
    }

}