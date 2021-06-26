package org.smartframework.cloud.api.core.annotation;

import java.lang.annotation.*;

/**
 * 重复提交校验注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRepeatSubmitCheck {

    /**
     * 执行完后30秒内有效
     */
    long TEN_SECONDS_AFTER_DONE = 30 * 1000L;

    /**
     * 重复提交校验有效期（默认为执行完后30秒内不允许重复提交。单位：毫秒）
     *
     * @return
     */
    long expireMillis() default TEN_SECONDS_AFTER_DONE;

}