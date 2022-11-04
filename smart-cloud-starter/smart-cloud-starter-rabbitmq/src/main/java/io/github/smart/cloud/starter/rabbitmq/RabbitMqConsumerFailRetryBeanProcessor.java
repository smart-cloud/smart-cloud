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

import io.github.smart.cloud.starter.core.constants.PackageConfig;
import io.github.smart.cloud.starter.rabbitmq.annotation.MqConsumerFailRetry;
import io.github.smart.cloud.starter.rabbitmq.util.MqNameUtil;
import io.github.smart.cloud.starter.rabbitmq.util.MqUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Set;

/**
 * 消费失败后的mq重试注册
 *
 * @author collin
 * @date 2021-07-03
 */
@Slf4j
public class RabbitMqConsumerFailRetryBeanProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        registerDelayMqBeans(beanFactory);
    }

    /**
     * 注册延迟队列bean
     *
     * @param beanFactory
     */
    private void registerDelayMqBeans(ConfigurableListableBeanFactory beanFactory) {
        Reflections reflections = new Reflections(PackageConfig.getBasePackages());
        Set<Class<? extends IRabbitMqConsumer>> mqConsumerClasses = reflections.getSubTypesOf(IRabbitMqConsumer.class);
        if (CollectionUtils.isEmpty(mqConsumerClasses)) {
            return;
        }

        for (Class<? extends IRabbitMqConsumer> mqConsumerClass : mqConsumerClasses) {
            if (registerDelayMqBean(mqConsumerClass, beanFactory)) {
                MqUtil.setEnableRetryAfterConsumerFail(true);
            }
        }
    }

    /**
     * 注册单个延迟队列bean
     *
     * @param mqConsumerClass
     * @param beanFactory
     */
    private boolean registerDelayMqBean(Class<?> mqConsumerClass, ConfigurableListableBeanFactory beanFactory) {
        MqConsumerFailRetry mqConsumerFailRetry = AnnotationUtils.findAnnotation(mqConsumerClass, MqConsumerFailRetry.class);
        if (mqConsumerFailRetry == null) {
            return false;
        }
        RabbitListener rabbitListener = AnnotationUtils.findAnnotation(mqConsumerClass, RabbitListener.class);
        if (rabbitListener == null) {
            log.warn("RabbitListener[{}] not found", mqConsumerClass.getSimpleName());
            return false;
        }

        // 队列的名称
        String queueName = rabbitListener.queues()[0];
        // 延迟路由键
        String retryRouteKeyName = MqNameUtil.getRetryRouteKeyName(queueName, mqConsumerFailRetry);
        // 延迟exchange
        String retryExchangeName = MqNameUtil.getRetryExchangeName(queueName, mqConsumerFailRetry);
        Exchange retryExchange = MqUtil.createDelayExchange(retryExchangeName);

        Queue queue = new Queue(queueName);
        Binding retryBinding = BindingBuilder.bind(queue).to(retryExchange).with(retryRouteKeyName).noargs();

        // exchange、binding需要注入IOC
        String retryBindingBeanName = MqConstants.BINDING_BEAN_NAME_PREFIX + MqConstants.DELAY_PREFIX + queueName;
        beanFactory.registerSingleton(retryBindingBeanName, retryBinding);
        beanFactory.registerSingleton(retryExchangeName, retryExchange);

        log.info("retryQueueInfo|queueName={},retryExchangeName={},retryRouteKeyName={}", queueName, retryExchangeName, retryRouteKeyName);

        return true;
    }

}