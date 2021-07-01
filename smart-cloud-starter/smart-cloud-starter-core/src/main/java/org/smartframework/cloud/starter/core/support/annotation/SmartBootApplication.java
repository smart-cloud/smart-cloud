package org.smartframework.cloud.starter.core.support.annotation;

import org.smartframework.cloud.starter.core.support.bean.UniqueBeanNameGenerator;
import org.smartframework.cloud.starter.core.support.condition.SmartBootApplicationCondition;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

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
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
@EnableAsync
@Conditional(SmartBootApplicationCondition.class)
public @interface SmartBootApplication {

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] componentBasePackages() default {};

    @AliasFor(annotation = EnableAutoConfiguration.class)
    Class<?>[] exclude() default {};

    @AliasFor(annotation = EnableAutoConfiguration.class)
    String[] excludeName() default {};

}