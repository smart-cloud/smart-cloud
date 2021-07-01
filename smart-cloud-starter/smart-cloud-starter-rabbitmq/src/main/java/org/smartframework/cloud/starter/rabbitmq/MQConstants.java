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
     * mq已重试次数
     */
    String CONSUMER_RETRIED_TIMES = "consumer_retried_times";
    /**
     * 幂等校验锁名称前缀
     */
    String IDE_CKECK_LOCK_NAME_PREFIX = "lock:mq:idecheck:";
    /**
     * 队列名后缀
     */
    String QUEUE_SUFFIX = "_queue";
    /**
     * 队列名后缀
     */
    String EXCHANGE_SUFFIX = "_exchange";
    /**
     * 队列名后缀
     */
    String ROUTEKEY_SUFFIX = "_routekey";
    /**
     * 延迟队列模式
     */
    String DELAY_MQ_PATTERN = "%s_delay%s";
    /**
     * mq消费失败后最大重试次数
     */
    int DEFAULT_MAX_RETRY_TIMES_WHEN_FAIL = 10;

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