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
package io.github.smart.cloud.starter.rate.limit;

import io.github.smart.cloud.exception.ParamValidateException;
import io.github.smart.cloud.starter.core.constants.PackageConfig;
import io.github.smart.cloud.starter.rate.limit.annotation.RateLimit;
import io.github.smart.cloud.starter.rate.limit.util.RateLimitUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * {@link Semaphore} bean注册
 *
 * @author collin
 * @date 2023-03-19
 */
public class SemaphoreBeanProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        registerSemaphoreBeans(beanFactory);
    }

    private void registerSemaphoreBeans(ConfigurableListableBeanFactory beanFactory) {
        Reflections reflections = new Reflections(PackageConfig.getBasePackages(), Scanners.MethodsAnnotated);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RateLimit.class);
        if (CollectionUtils.isEmpty(methods)) {
            return;
        }

        for (Method method : methods) {
            RateLimit rateLimit = method.getAnnotation(RateLimit.class);
            if (rateLimit.rate() == 0) {
                String tip = String.format("the rate of RateLimit[%s.%s] must greater than 0!", method.getDeclaringClass().getName(), method.getName());
                throw new ParamValidateException(tip);
            }

            String rateLimitBeanName = RateLimitUtil.getSemaphoreBeanName(method, rateLimit);
            beanFactory.registerSingleton(rateLimitBeanName, new Semaphore(rateLimit.rate()));
        }
    }

}