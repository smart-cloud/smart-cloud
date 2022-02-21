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
package io.github.smart.cloud.starter.rabbitmq.util;

import io.github.smart.cloud.starter.rabbitmq.MqConstants;
import io.github.smart.cloud.starter.rabbitmq.annotation.MqConsumerFailRetry;
import org.apache.commons.lang3.StringUtils;

/**
 * mq名称工具类
 *
 * @author collin
 * @date 2021-07-01
 */
public final class MqNameUtil {

    private MqNameUtil() {
    }

    public static final String getRetryExchangeName(String queueName, MqConsumerFailRetry mqConsumerFailRetry) {
        if (StringUtils.isNotBlank(mqConsumerFailRetry.retryExchange())) {
            return mqConsumerFailRetry.retryExchange();
        }
        return MqConstants.DELAY_PREFIX + queueName + MqConstants.EXCHANGE_SUFFIX;
    }

    public static final String getRetryRouteKeyName(String queueName, MqConsumerFailRetry mqConsumerFailRetry) {
        if (StringUtils.isNotBlank(mqConsumerFailRetry.retryRouteKey())) {
            return mqConsumerFailRetry.retryRouteKey();
        }
        return MqConstants.DELAY_PREFIX + queueName + MqConstants.ROUTE_KEY_SUFFIX;
    }

}