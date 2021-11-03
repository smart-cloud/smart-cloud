package org.smartframework.cloud.starter.rabbitmq.annotation;

import org.smartframework.cloud.starter.rabbitmq.MqConstants;

import java.lang.annotation.*;

/**
 * mq消费失败后重试
 *
 * @author collin
 * @date 2021-06-30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MqConsumerFailRetry {

    /**
     * 重试交换机名字
     */
    String retryExchange() default "";

    /**
     * 延迟路由键
     */
    String delayRouteKey() default "";

    /**
     * 消费失败后最大的重试次数
     *
     * @return
     */
    int maxRetryTimes() default MqConstants.DEFAULT_MAX_RETRY_TIMES_WHEN_FAIL;

}