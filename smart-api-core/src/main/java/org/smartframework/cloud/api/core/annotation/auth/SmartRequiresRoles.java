package org.smartframework.cloud.api.core.annotation.auth;

import java.lang.annotation.*;

/**
 * 需要的角色注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmartRequiresRoles {

    String[] value();

}