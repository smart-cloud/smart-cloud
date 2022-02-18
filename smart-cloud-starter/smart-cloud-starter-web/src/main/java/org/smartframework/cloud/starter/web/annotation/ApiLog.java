package org.smartframework.cloud.starter.web.annotation;

import org.smartframework.cloud.constants.LogLevel;

import java.lang.annotation.*;

/**
 * 接口日志打印注解
 *
 * @author collin
 * @date 2021-03-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {

    /**
     * 日志级别（默认info级别）
     *
     * @return
     */
    String level() default LogLevel.INFO;

}