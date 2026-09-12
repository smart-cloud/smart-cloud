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
package io.github.smart.cloud.starter.trace.debug.pointcut;

import io.github.smart.cloud.starter.trace.debug.annotation.TraceDebug;
import io.github.smart.cloud.starter.trace.debug.properties.TraceDebugPointCutProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 链路切面点
 *
 * @author collin.li
 * @date 2026-01-07
 */
@Slf4j
@RequiredArgsConstructor
public class TraceDebugPointCut extends StaticMethodMatcherPointcut {
    /**
     * 类切面
     */
    private final TraceDebugPointCutProperties traceDebugPointCutProperties;
    /**
     * FeignClient注解类名
     */
    private static final String FEIGN_CLIENT_ANNOTATION_NAME = "org.springframework.cloud.openfeign.FeignClient";
    /**
     * mybatis Mapper注解类名
     */
    private static final String MYBATIS_MAPPER_ANNOTATION_NAME = "org.apache.ibatis.annotations.Mapper";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        String className = targetClass.getName();
        // 要排除的类
        if (traceDebugPointCutProperties.getExcludeClassSet().contains(className)) {
            return false;
        }

        // 要匹配的类
        if (traceDebugPointCutProperties.getMatchClassSet().contains(className)) {
            return true;
        }

        // TraceDebug注解匹配
        TraceDebug traceDebug = method.getAnnotation(TraceDebug.class);
        if (traceDebug == null) {
            traceDebug = method.getDeclaringClass().getAnnotation(TraceDebug.class);
        }
        if (traceDebug != null) {
            return traceDebug.enable();
        }

        // feign客户端
        if (AnnotatedElementUtils.isAnnotated(method.getDeclaringClass(), FEIGN_CLIENT_ANNOTATION_NAME)) {
            return true;
        }

        // 排除final、非public类（不能生成代理）
        if (Modifier.isFinal(targetClass.getModifiers()) || !Modifier.isPublic(targetClass.getModifiers())) {
            return false;
        }

        // spring实例对象
        return AnnotatedElementUtils.isAnnotated(targetClass, RestController.class)
                || AnnotatedElementUtils.isAnnotated(targetClass, Controller.class)
                || AnnotatedElementUtils.isAnnotated(targetClass, Service.class)
                || AnnotatedElementUtils.isAnnotated(targetClass, Component.class)
                || AnnotatedElementUtils.isAnnotated(targetClass, Repository.class)
                || AnnotatedElementUtils.isAnnotated(targetClass, MYBATIS_MAPPER_ANNOTATION_NAME);
    }

}