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
package io.github.smart.cloud.starter.job.trace;

import brave.ScopedSpan;
import brave.Tracing;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.internal.DefaultSpanNamer;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;

/**
 * xxl-job日志链路号配置类
 *
 * @author collin
 * @date 2021-04-11
 */
@Slf4j
@Configuration
@ConditionalOnClass({ScopedSpan.class, Tracing.class, SpanNamer.class, DefaultSpanNamer.class})
@ConditionalOnProperty(name = "smart.xxlJob.trace.enabled", havingValue = "true", matchIfMissing = true)
public class XxlJobBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 不能直接用XxjobHnadlerTraceWrapper进行包装，因bean被其他对象引用，引用校验因类型不匹配会报错，所以只能通过代理方式
        return bean instanceof IJobHandler ? wrap(bean) : bean;
    }

    private Object wrap(Object bean) {
        boolean classFinal = Modifier.isFinal(bean.getClass().getModifiers());
        boolean cglibProxy = !classFinal;
        IJobHandler job = (IJobHandler) bean;
        try {
            return createProxy(bean, cglibProxy, new XxlJobMethodInterceptor<IJobHandler>(job, beanFactory));
        } catch (AopConfigException ex) {
            if (cglibProxy) {
                log.warn("Exception occurred while trying to create a proxy, falling back to JDK proxy", ex);
                return createProxy(bean, false, new XxlJobMethodInterceptor<IJobHandler>(job, this.beanFactory));
            }
            throw ex;
        }
    }

    /**
     * 创建xxl-job代理
     *
     * @param bean
     * @param cglibProxy
     * @param advice
     * @return
     */
    private Object createProxy(Object bean, boolean cglibProxy, Advice advice) {
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setProxyTargetClass(cglibProxy);
        factory.addAdvice(advice);
        factory.setTarget(bean);
        return factory.getObject();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}