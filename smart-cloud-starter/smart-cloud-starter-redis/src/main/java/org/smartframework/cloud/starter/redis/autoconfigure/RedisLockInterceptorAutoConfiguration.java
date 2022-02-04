package org.smartframework.cloud.starter.redis.autoconfigure;

import org.redisson.api.RedissonClient;
import org.smartframework.cloud.starter.redis.annotation.RedisLock;
import org.smartframework.cloud.starter.redis.intercept.RedisLockInterceptor;
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