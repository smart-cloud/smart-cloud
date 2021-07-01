package org.smartframework.cloud.starter.rabbitmq.test.mq;

import org.smartframework.cloud.starter.rabbitmq.MQConstants;

public interface MQConstant {

    /**
     * 活动发券mq
     */
    interface SendCoupon {
        String BASE_PREFIX = "send_coupon";
        String QUEUE = BASE_PREFIX + MQConstants.QUEUE_SUFFIX;
        String EXCHANGE = BASE_PREFIX + MQConstants.EXCHANGE_SUFFIX;
        String ROUTING = BASE_PREFIX + MQConstants.ROUTEKEY_SUFFIX;
    }

}