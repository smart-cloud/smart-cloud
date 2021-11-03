package org.smartframework.cloud.starter.rabbitmq.test.mq;

import org.smartframework.cloud.starter.rabbitmq.MqConstants;

public interface MqConstant {

    /**
     * 活动发券mq
     */
    interface SendCoupon {
        String BASE_PREFIX = "send_coupon";
        String QUEUE = BASE_PREFIX + MqConstants.QUEUE_SUFFIX;
        String EXCHANGE = BASE_PREFIX + MqConstants.EXCHANGE_SUFFIX;
        String ROUTING = BASE_PREFIX + MqConstants.ROUTEKEY_SUFFIX;
    }

}