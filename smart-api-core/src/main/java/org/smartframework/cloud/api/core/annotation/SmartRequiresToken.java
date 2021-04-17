package org.smartframework.cloud.api.core.annotation;

import java.lang.annotation.*;

/**
 * token校验注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmartRequiresToken {
}