/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.rabbitmq.annotation;

import io.github.smart.cloud.starter.rabbitmq.MqConstants;

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
     * 重试路由键名字
     */
    String retryRouteKey() default "";

    /**
     * 消费失败后最大的重试次数
     *
     * @return
     */
    int maxRetryTimes() default MqConstants.DEFAULT_MAX_RETRY_TIMES_WHEN_FAIL;

}