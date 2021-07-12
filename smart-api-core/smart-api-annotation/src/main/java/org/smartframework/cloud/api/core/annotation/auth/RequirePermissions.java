package org.smartframework.cloud.api.core.annotation.auth;

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
public @interface RequirePermissions {

    /**
     * 权限编码
     *
     * @return
     */
    String[] value();

}