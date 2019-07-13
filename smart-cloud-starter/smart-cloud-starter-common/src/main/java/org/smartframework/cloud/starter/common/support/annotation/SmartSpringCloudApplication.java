package org.smartframework.cloud.starter.common.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smartframework.cloud.starter.common.support.bean.UniqueBeanNameGenerator;
import org.smartframework.cloud.starter.common.support.condition.SmartSpringCloudApplicationCondition;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 服务启动类注解
 *
 * @author liyulin
 * @date 2019-03-31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(nameGenerator = UniqueBeanNameGenerator.class, excludeFilters = {
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableAsync
@Conditional(SmartSpringCloudApplicationCondition.class)
public @interface SmartSpringCloudApplication {

	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] componentBasePackages() default { "${smart.component.basePackages:}" };

	@AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
	String[] feignClientBasePackages() default { "${smart.feign.basePackages:}" };

	@AliasFor(annotation = EnableAutoConfiguration.class)
	Class<?>[] exclude() default {};
	
	@AliasFor(annotation = EnableAutoConfiguration.class)
	String[] excludeName() default { "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
			"org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration" };

}