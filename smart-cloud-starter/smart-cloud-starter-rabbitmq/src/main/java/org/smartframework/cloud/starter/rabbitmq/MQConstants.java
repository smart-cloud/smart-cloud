package org.smartframework.cloud.starter.rabbitmq;

/**
 * mq常量
 *
 * @author collin
 * @date 2021-06-17
 */
public interface MQConstants {

    /**
     * rabbitmq header中的消息id
     */
    String MESSAGE_ID_NAME = "id";
    /**
     * 幂等校验锁名称前缀
     */
    String IDE_CKECK_LOCK_NAME_PREFIX = "lock:mq:idecheck:";

    /**
     * 死信队列参数
     */
    interface DeadLetterQueueArgs {
        /**
         * 死信交换机
         */
        String EXCHANGE = "x-dead-letter-exchange";
        /**
         * 死信路由键
         */
        String ROUTING_KEY = "x-dead-letter-routing-key";
    }

}