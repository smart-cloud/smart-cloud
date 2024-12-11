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

import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.starter.core.util.ReflectionUtil;
import io.github.smart.cloud.starter.rate.limit.annotation.RateLimiter;
import io.github.smart.cloud.starter.rate.limit.properties.RateLimitProperties;
import io.github.smart.cloud.starter.rate.limit.util.RateLimitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * 信号量限流实例工厂
 *
 * @author collin
 * @date 2024-02-28
 */
@Slf4j
@RequiredArgsConstructor
public class RateLimitInstanceFactory implements InitializingBean {

    private final RateLimitProperties rateLimitProperties;
    private static final Map<String, Semaphore> RATE_LIMIT_INSTANCES = new ConcurrentHashMap<>(4);

    @Override
    public void afterPropertiesSet() throws Exception {
        initRateLimitBeans();
    }

    public Semaphore get(String name) {
        return RATE_LIMIT_INSTANCES.get(name);
    }

    /**
     * 初始化限流bean实例
     */
    private void initRateLimitBeans() {
        Map<String, Integer> rateLimitConfig = new HashMap<>(4);

        // 注解限流
        Set<Method> methods = ReflectionUtil.getMethodsAnnotatedWith(RateLimiter.class);
        if (!CollectionUtils.isEmpty(methods)) {
            methods.forEach(method -> {
                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
                rateLimitConfig.put(RateLimitUtil.getSemaphoreBeanName(method), rateLimiter.permits());
            });
        }

        // 配置限流
        Map<String, Integer> config = rateLimitProperties.getConfig();
        if (!CollectionUtils.isEmpty(config)) {
            removeInvalidRateLimitRule(config);
            if (!CollectionUtils.isEmpty(config)) {
                rateLimitConfig.putAll(config);
            }
        }

        if (CollectionUtils.isEmpty(rateLimitConfig)) {
            log.warn("rate limit config is empty.");
            return;
        }

        // 移除待删除掉的
        Set<String> oldNames = RATE_LIMIT_INSTANCES.keySet();
        Set<String> needRemoveBeanNames = oldNames.stream().filter(name -> !rateLimitConfig.containsKey(name)).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(needRemoveBeanNames)) {
            needRemoveBeanNames.forEach(RATE_LIMIT_INSTANCES::remove);
        }

        // 添加/更新新的
        log.info("---->refresh ratelimit rule start");
        rateLimitConfig.forEach((name, permits) -> RATE_LIMIT_INSTANCES.put(name, new Semaphore(permits)));
        log.info("---->refresh ratelimit rule end");
    }

    /**
     * 移除配置无效的限流规则
     *
     * @param rateLimitConfig
     */
    private void removeInvalidRateLimitRule(Map<String, Integer> rateLimitConfig) {
        Set<String> rateLimitBeanNames = rateLimitConfig.keySet();
        for (String rateLimitBeanName : rateLimitBeanNames) {
            int lastDotIndex = rateLimitBeanName.lastIndexOf(SymbolConstant.DOT);
            if (lastDotIndex == -1) {
                log.warn("The format of bean name[{}] is error", rateLimitBeanName);
                rateLimitConfig.remove(rateLimitBeanName);
                continue;
            }

            String className = rateLimitBeanName.substring(0, lastDotIndex);
            Class<?> c = null;
            try {
                c = Class.forName(className, false, RateLimitInstanceFactory.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                log.warn("The class of bean name[{}] is not found", rateLimitBeanName);
                rateLimitConfig.remove(rateLimitBeanName);
                continue;
            }

            Method[] methods = c.getMethods();
            String methodName = rateLimitBeanName.substring(lastDotIndex + 1);
            if (!existMatchMethod(methods, methodName)) {
                log.warn("The method of bean name[{}] is not found", rateLimitBeanName);
                rateLimitConfig.remove(rateLimitBeanName);
            }
        }
    }

    /**
     * 是否存在有匹配的方法
     *
     * @param methods
     * @param methodName
     * @return
     */
    private boolean existMatchMethod(Method[] methods, String methodName) {
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                return true;
            }
        }
        return false;
    }

}