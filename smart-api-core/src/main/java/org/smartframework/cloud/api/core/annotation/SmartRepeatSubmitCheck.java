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
public @interface SmartRepeatSubmitCheck {

    /**
     * 永久有效
     */
    public static final long FOREVER = -1;

    /**
     * 重复提交校验有效期（默认为10秒；-1为永久有效）
     *
     * @return
     */
    long expireMillis() default 10000L;

}