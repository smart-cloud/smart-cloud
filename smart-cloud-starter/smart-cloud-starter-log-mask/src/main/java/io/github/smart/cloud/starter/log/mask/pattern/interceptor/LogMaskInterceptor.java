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
package io.github.smart.cloud.starter.log.mask.pattern.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.smart.cloud.starter.log.mask.pattern.annotation.LogMask;
import io.github.smart.cloud.starter.log.mask.pattern.enums.MaskMode;
import io.github.smart.cloud.starter.log.mask.pattern.properties.LogMaskProperties;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.binding.MapperProxy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

/**
 * 日志脱敏规则方法拦截器
 *
 * @author collin.li
 * @date 2025-12-23
 */
@Slf4j
@RequiredArgsConstructor
public class LogMaskInterceptor implements MethodInterceptor {

    private final LogMaskProperties logMaskProperties;
    private static final Cache<Method, LogMask> LOG_MASK_CACHE = Caffeine.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(25, TimeUnit.HOURS)
            .build();
    private static final LogMask DEFAULT_LOGMASK = AnnotationUtils.synthesizeAnnotation(LogMask.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 非注解脱敏
        Integer mode = logMaskProperties.getMode();
        if (mode == null || (MaskMode.ANNOTATION.getValue().compareTo(mode) != 0
                && MaskMode.FULL.getValue().compareTo(mode) != 0)) {
            return invocation.proceed();
        }

        LogMask logMask = null;
        try {
            logMask = getLogMask(invocation);
        } catch (Exception e) {
            log.warn("getLogMask error|method={}#{}", invocation.getMethod().getDeclaringClass(), invocation.getMethod().getName(), e);
        }
        if (logMask == null && MaskMode.FULL.getValue().compareTo(mode) == 0) {
            logMask = DEFAULT_LOGMASK;
        }
        if (logMask == null) {
            return invocation.proceed();
        }

        Pattern previousPattern = LogMaskContext.get();
        try {
            LogMaskContext.set(logMask.regex());
            return invocation.proceed();
        } finally {
            if (logMask.cleanAfter()) {
                if (previousPattern == null) {
                    LogMaskContext.remove();
                } else {
                    LogMaskContext.set(previousPattern);
                }
            }
        }
    }

    /**
     * 获取日志脱敏注解
     *
     * @param invocation
     * @return
     */
    private LogMask getLogMask(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        // 1、优先从缓存获取
        LogMask logMask = LOG_MASK_CACHE.getIfPresent(method);
        if (logMask != null) {
            return logMask;
        }

        // 2、先从方法获取
        logMask = AnnotationUtils.findAnnotation(method, LogMask.class);
        if (logMask != null) {
            LOG_MASK_CACHE.put(method, logMask);
            return logMask;
        }

        // 3、最后从类获取
        logMask = AnnotationUtils.findAnnotation(method.getDeclaringClass(), LogMask.class);
        if (logMask != null) {
            LOG_MASK_CACHE.put(method, logMask);
            return logMask;
        }

        // 兼容mybatis dao
        if (invocation.getThis() instanceof Proxy) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(invocation.getThis());
            if (invocationHandler instanceof MapperProxy) {
                MapperProxy<?> mapperProxy = (MapperProxy<?>) invocationHandler;
                Field mapperInterfaceField = ReflectionUtils.findField(MapperProxy.class, "mapperInterface");
                mapperInterfaceField.setAccessible(true);
                Class<?> mapperInterface = (Class<?>) ReflectionUtils.getField(mapperInterfaceField, mapperProxy);
                logMask = AnnotationUtils.findAnnotation(mapperInterface, LogMask.class);
                if (logMask != null) {
                    LOG_MASK_CACHE.put(method, logMask);
                    return logMask;
                }
                // In ANNOTATION mode an unannotated mapper must not be treated as FULL mode.
                return null;
            }
        }

        return null;
    }

}