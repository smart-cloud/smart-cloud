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
import io.github.smart.cloud.starter.rate.limit.RateLimitInstanceFactory;
import io.github.smart.cloud.starter.rate.limit.annotation.RateLimiter;
import io.github.smart.cloud.starter.rate.limit.util.RateLimitUtil;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

/**
 * 限流拦截器
 *
 * @author collin
 * @date 2023-03-19
 */
@RequiredArgsConstructor
public class RateLimitInterceptor implements MethodInterceptor {

    private final RateLimitInstanceFactory rateLimitInstanceFactory;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String rateLimitBeanName = RateLimitUtil.getSemaphoreBeanName(method);
        Semaphore semaphore = rateLimitInstanceFactory.get(rateLimitBeanName);
        if (semaphore == null) {
            return invocation.proceed();
        }

        boolean isAcquire = false;
        try {
            isAcquire = semaphore.tryAcquire();
            if (!isAcquire) {
                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
                if (rateLimiter != null && StringUtils.isNotBlank(rateLimiter.message())) {
                    throw new AccessFrequentlyException(rateLimiter.message());
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

}