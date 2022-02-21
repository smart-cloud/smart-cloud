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
     * Binding bean名称前缀
     */
    String BINDING_BEAN_NAME_PREFIX = "bind";

    /**
     * 延迟交换机类型key
     */
    String DELAY_EXCHANGE_TYPE_KEY = "x-delayed-type";

    /**
     * 延迟消息类型
     */
    String DELAY_MESSAGE_TYPE = "x-delayed-message";

    /**
     * 延迟队列（交换机、路由键等）名称前缀
     */
    String DELAY_PREFIX = "delay_";

    /**
     * 交换机后缀
     */
    String EXCHANGE_SUFFIX = "_exchange";

    /**
     * 路由键后缀
     */
    String ROUTE_KEY_SUFFIX = "_route_key";
    /**
     * mq消费失败后最大重试次数
     */
    int DEFAULT_MAX_RETRY_TIMES_WHEN_FAIL = 10;

}