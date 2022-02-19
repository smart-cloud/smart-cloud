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
package io.github.smart.cloud.starter.redis.autoconfigure;

import io.github.smart.cloud.starter.redis.intercept.RedisLockInterceptor;
import org.redisson.api.RedissonClient;
import io.github.smart.cloud.starter.redis.annotation.RedisLock;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link RedisLock}拦截器配置
 *
 * @author collin
 * @date 2022-02-02
 */
@Configuration
public class RedisLockInterceptorAutoConfiguration {

    @Bean
    public RedisLockInterceptor redisLockInterceptor(final RedissonClient redissonClient) {
        return new RedisLockInterceptor(redissonClient);
    }

    @Bean
    public Pointcut redisLockPointcut() {
        AspectJExpressionPointcut redisLockPointcut = new AspectJExpressionPointcut();
        redisLockPointcut.setExpression(String.format("@annotation(%s)", RedisLock.class.getTypeName()));
        return redisLockPointcut;
    }

    @Bean
    public Advisor redisLockAdvisor(final RedisLockInterceptor redisLockInterceptor, final Pointcut redisLockPointcut) {
        DefaultBeanFactoryPointcutAdvisor redisLockAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        redisLockAdvisor.setAdvice(redisLockInterceptor);
        redisLockAdvisor.setPointcut(redisLockPointcut);

        return redisLockAdvisor;
    }

}