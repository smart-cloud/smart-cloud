package org.smartframework.cloud.api.core.annotation;

import org.smartframework.cloud.api.core.enums.Logical;

import java.lang.annotation.*;

/**
 * 权限校验注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmartRequiresAuth {

    String[] value();

    Logical logical() default Logical.AND;

}