package org.smartframework.cloud.api.core.annotation;

import org.smartframework.cloud.api.core.annotation.constants.ApiAnnotationConstants;

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
     * 重复提交校验有效期（如果大于0，表示执行完后expireMillis毫秒内不允许重复提交。单位：毫秒）
     *
     * @return
     */
    long expireMillis() default ApiAnnotationConstants.DEFAULT_EXPIRE_MILLIS_OF_REPEAT_SUBMIT;

}