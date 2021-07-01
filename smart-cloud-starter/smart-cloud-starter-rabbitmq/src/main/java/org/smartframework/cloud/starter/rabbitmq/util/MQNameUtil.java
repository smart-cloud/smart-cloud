package org.smartframework.cloud.starter.rabbitmq.util;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.rabbitmq.MQConstants;
import org.smartframework.cloud.starter.rabbitmq.annotation.MQConsumerFailRetry;

/**
 * mq名称工具类
 *
 * @author collin
 * @date 2021-07-01
 */
public final class MQNameUtil {

    public static final String getQueuePrefix(String queueName) {
        return queueName.substring(0, queueName.length() - MQConstants.QUEUE_SUFFIX.length());
    }

    public static final String getRetryExchangeName(String retryQueuePrefix, MQConsumerFailRetry mqConsumerFailRetry) {
        return StringUtils.isNotBlank(mqConsumerFailRetry.retryExchange()) ? mqConsumerFailRetry.retryExchange() : String.format("%s%s", retryQueuePrefix, MQConstants.EXCHANGE_SUFFIX);
    }

    public static final String getRetryRouteKeyName(String retryQueuePrefix) {
        return String.format("%s%s", retryQueuePrefix, MQConstants.ROUTEKEY_SUFFIX);
    }

    public static final String getDelayQueueName(String retryQueuePrefix) {
        return String.format(MQConstants.DELAY_MQ_PATTERN, retryQueuePrefix, MQConstants.QUEUE_SUFFIX);
    }

    public static final String getDelayRouteKeyName(String retryQueuePrefix, MQConsumerFailRetry mqConsumerFailRetry) {
        String delayRouteKeyName = mqConsumerFailRetry.delayRouteKey();
        if (StringUtils.isBlank(delayRouteKeyName)) {
            delayRouteKeyName = String.format(MQConstants.DELAY_MQ_PATTERN, retryQueuePrefix, MQConstants.ROUTEKEY_SUFFIX);
        }
        return delayRouteKeyName;
    }

}