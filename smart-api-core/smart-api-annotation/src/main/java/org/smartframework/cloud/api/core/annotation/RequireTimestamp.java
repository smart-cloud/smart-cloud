package org.smartframework.cloud.api.core.annotation;

import org.smartframework.cloud.api.core.annotation.constants.ApiAnnotationConstants;

import java.lang.annotation.*;

/**
 * 请求时间戳校验注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireTimestamp {

    /**
     * header中请求有效时间间隔（默认2分钟，单位毫秒）
     */
    long validMillis() default ApiAnnotationConstants.DEFAULT_TIMESTAMP_VALID_MILLIS;

}