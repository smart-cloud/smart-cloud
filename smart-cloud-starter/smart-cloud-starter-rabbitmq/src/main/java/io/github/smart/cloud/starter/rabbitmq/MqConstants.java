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
package io.github.smart.cloud.starter.rabbitmq;

/**
 * mq常量
 *
 * @author collin
 * @date 2021-06-17
 */
public interface MqConstants {

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