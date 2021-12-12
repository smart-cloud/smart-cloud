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
package org.smartframework.cloud.starter.rpc.dubbo.condition;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.smartframework.cloud.starter.core.business.util.ReflectionUtil;
import org.smartframework.cloud.utility.ObjectUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author collin
 * @desc dubbo rpc实例化条件判断
 * @date 2019/09/18
 */
@Slf4j
public class DubboCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 修饰类
        if (metadata instanceof ClassMetadata) {
            return classMatch((ClassMetadata) metadata);
        } else if (metadata instanceof MethodMetadata) {
            // 修饰方法
            return methodMatch((MethodMetadata) metadata);
        }

        throw new UnsupportedOperationException();
    }

    /**
     * 条件注解 修饰class
     *
     * @param metadata
     * @return
     */
    private boolean classMatch(ClassMetadata metadata) {
        String className = metadata.getClassName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getReturnType() != ReferenceBean.class) {
                continue;
            }
            if (matchWithMethod(method)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 条件注解 修饰method
     *
     * @param metadata
     * @return
     */
    private boolean methodMatch(MethodMetadata metadata) {
        String className = metadata.getDeclaringClassName();
        Class<?> declaringClass = null;
        try {
            declaringClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        Method method = null;
        try {
            method = declaringClass.getDeclaredMethod(metadata.getMethodName());
        } catch (NoSuchMethodException | SecurityException e) {
            log.error(e.getMessage(), e);
        }

        return matchWithMethod(method);
    }

    private boolean matchWithMethod(Method method) {
        // 获取rpc类型
        Type type = method.getGenericReturnType();
        if (!(type instanceof ParameterizedType)) {
            throw new UnsupportedOperationException(String.format("获取不到%s的泛型值", type.getTypeName()));
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actualType = parameterizedType.getActualTypeArguments()[0];
        Class<?> rpcClass = null;
        try {
            rpcClass = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        // 获取interface对应的所有实现类
        Set<?> subTypes = ReflectionUtil.getSubTypesOf(rpcClass);

        // 判断是否存在RPC interface的实现类，且实现类上有Service注解
        if (subTypes == null || subTypes.isEmpty()) {
            return true;
        }
        // 遍历bean
        for (Object subType : subTypes) {
            Class<?> subTypeClass = (Class<?>) subType;
            boolean isRpcImplementClass = isDubboRpcImplementClass(subTypeClass);
            if (isRpcImplementClass) {
                return false;
            }
        }

        return true;
    }

    private boolean isDubboRpcImplementClass(Class<?> clazz) {
        return ObjectUtil.isNotNull(AnnotationUtils.findAnnotation(clazz, DubboService.class));
    }

}