package org.smartframework.cloud.starter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.rabbitmq.annotation.MqConsumerFailRetry;
import org.smartframework.cloud.starter.rabbitmq.util.MqNameUtil;
import org.smartframework.cloud.starter.rabbitmq.util.MqUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;
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
        Set<Class<? extends AbstractRabbitMqConsumerMarker>> mqConsumerClasses = reflections.getSubTypesOf(AbstractRabbitMqConsumerMarker.class);
        if (CollectionUtils.isEmpty(mqConsumerClasses)) {
            return;
        }

        for (Class<? extends AbstractRabbitMqConsumerMarker> mqConsumerClass : mqConsumerClasses) {
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
        String retryQueueName = rabbitListener.queues()[0];
        if (!retryQueueName.endsWith(MqConstants.QUEUE_SUFFIX)) {
            throw new UnsupportedOperationException(String.format("The suffix of the queue[%s] name that needs to be retried must be %s", mqConsumerClass, MqConstants.QUEUE_SUFFIX));
        }
        String retryQueuePrefix = MqNameUtil.getQueuePrefix(retryQueueName);
        //交换机名称
        String retryExchangeName = MqNameUtil.getRetryExchangeName(retryQueuePrefix, mqConsumerFailRetry);
        //路由键
        String retryRouteKeyName = MqNameUtil.getRetryRouteKeyName(retryQueuePrefix);

        //延迟队列名称
        String delayQueueName = MqNameUtil.getDelayQueueName(retryQueuePrefix);
        //延迟路由键
        String delayRouteKeyName = MqNameUtil.getDelayRouteKeyName(retryQueuePrefix, mqConsumerFailRetry);
        // exchange已存在，不用注入IOC
        Exchange retryExchange = ExchangeBuilder.directExchange(retryExchangeName).durable(true).build();
        Queue delayQueue = buildDelayQueue(delayQueueName, retryExchangeName, retryRouteKeyName);
        Binding delayBinding = BindingBuilder.bind(delayQueue).to(retryExchange).with(delayRouteKeyName).noargs();

        // queue、binding需要注入IOC
        beanFactory.registerSingleton(delayQueueName, delayQueue);
        String delayBindingBeanName = String.format(MqConstants.DELAY_MQ_PATTERN, retryQueuePrefix, Binding.class.getSimpleName());
        beanFactory.registerSingleton(delayBindingBeanName, delayBinding);

        log.info("retryQueueInfo|retryQueueName={},retryExchangeName={},retryRouteKeyName={},delayQueueName={},delayRouteKeyName={}", retryQueueName,
                retryExchangeName, retryRouteKeyName, delayQueueName, delayRouteKeyName);

        return true;
    }

    /**
     * 构造延迟队列
     *
     * @param delayQueue
     * @param retryExchange
     * @param retryRouteKey
     * @return
     */
    private Queue buildDelayQueue(String delayQueue, String retryExchange, String retryRouteKey) {
        Map<String, Object> args = new HashMap<>(2);
        // 声明死信交换机：x-dead-letter-exchange
        args.put(MqConstants.DeadLetterQueueArgs.EXCHANGE, retryExchange);
        // 声明死信路由键：x-dead-letter-routing-key
        args.put(MqConstants.DeadLetterQueueArgs.ROUTING_KEY, retryRouteKey);
        return QueueBuilder.durable(delayQueue).withArguments(args).build();
    }

}