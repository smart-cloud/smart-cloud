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
package io.github.smart.cloud.starter.rate.limit.intercept;

import io.github.smart.cloud.exception.AccessFrequentlyException;
import io.github.smart.cloud.starter.rate.limit.annotation.RateLimit;
import io.github.smart.cloud.starter.rate.limit.util.RateLimitUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

/**
 * 限流拦截器
 *
 * @author collin
 * @date 2023-03-19
 */
public class RateLimitInterceptor implements MethodInterceptor, BeanFactoryPostProcessor, ApplicationContextAware {

    private ConfigurableListableBeanFactory beanFactory;
    private ApplicationContext applicationContext;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        Semaphore semaphore = getSemaphore(method, rateLimit);
        boolean isAcquire = false;
        try {
            isAcquire = semaphore.tryAcquire();
            if (!isAcquire) {
                String message = rateLimit.message();
                if (StringUtils.isNotBlank(message)) {
                    throw new AccessFrequentlyException(message);
                } else {
                    throw new AccessFrequentlyException();
                }
            }

            return invocation.proceed();
        } finally {
            if (isAcquire) {
                semaphore.release();
            }
        }
    }

    private Semaphore getSemaphore(Method method, RateLimit rateLimit) {
        String rateLimitBeanName = RateLimitUtil.getSemaphoreBeanName(method, rateLimit);
        try {
            return applicationContext.getBean(rateLimitBeanName, Semaphore.class);
        } catch (NoSuchBeanDefinitionException e) {
            synchronized (this) {
                if (applicationContext.containsBean(rateLimitBeanName)) {
                    return applicationContext.getBean(rateLimitBeanName, Semaphore.class);
                } else {
                    Semaphore semaphore = new Semaphore(rateLimit.permits());
                    beanFactory.registerSingleton(rateLimitBeanName, semaphore);
                    return semaphore;
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}