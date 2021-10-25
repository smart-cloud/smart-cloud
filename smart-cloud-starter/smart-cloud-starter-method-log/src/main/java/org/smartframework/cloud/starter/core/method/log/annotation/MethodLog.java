package org.smartframework.cloud.starter.core.method.log.annotation;

import java.lang.annotation.*;

/**
 * method日志打印注解
 *
 * @author collin
 * @date 2021-03-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {
}