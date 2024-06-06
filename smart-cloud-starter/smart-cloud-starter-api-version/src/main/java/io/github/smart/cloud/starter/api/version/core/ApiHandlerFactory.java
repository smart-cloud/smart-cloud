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
package io.github.smart.cloud.starter.api.version.core;

import io.github.smart.cloud.starter.api.version.annotation.ApiHandlerMethod;
import io.github.smart.cloud.starter.api.version.annotation.ApiHandlerVersion;
import io.github.smart.cloud.starter.api.version.dto.ApiHandlerDTO;
import io.github.smart.cloud.starter.api.version.exception.ApiHandlerMethodMissingException;
import io.github.smart.cloud.starter.api.version.exception.ApiHandlerNotFoundException;
import io.github.smart.cloud.starter.api.version.exception.DuplicateApiHandlerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口版本控制工厂
 *
 * @author collin
 * @date 2024-06-06
 */
@Slf4j
public class ApiHandlerFactory implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;
    private Map<String, ApiHandlerDTO> apiVersionBeanRoute = new HashMap<>();

    /**
     * 多版本接口调用入口
     *
     * @param apiName    handler名称
     * @param apiVersion 接口版本号（如v1、v2）
     * @param params     接口handler参数
     * @param <T>
     * @return
     */
    public <T> T handle(String apiName, String apiVersion, Object[] params) {
        String routeKey = buildRouteKey(apiName, apiVersion);
        ApiHandlerDTO apiHandlerDTO = apiVersionBeanRoute.get(routeKey);
        if (apiHandlerDTO == null) {
            throw new ApiHandlerNotFoundException(apiName + ":" + apiVersion);
        }

        try {
            return (T) apiHandlerDTO.getHandlerMethod().invoke(apiHandlerDTO.getHandler(), params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ApiHandlerVersion.class);
        if (beanMap.isEmpty()) {
            log.warn("bean with ApiHandlerVersion annotation is empty!");
            return;
        }

        Collection<Object> beans = beanMap.values();
        for (Object bean : beans) {
            Class<?> beanClass = bean.getClass();
            ApiHandlerVersion apiHandlerVersion = AnnotationUtils.findAnnotation(beanClass, ApiHandlerVersion.class);
            String routeKey = buildRouteKey(apiHandlerVersion.routeKeyPrefix(), "v" + apiHandlerVersion.version());
            Method apiHanlerMethod = getApiHandleMethod(beanClass, apiHandlerVersion);

            check(beanClass, routeKey);

            apiVersionBeanRoute.put(routeKey, new ApiHandlerDTO(bean, apiHanlerMethod));
        }
    }

    private String buildRouteKey(String routeKeyPrefix, String version) {
        return routeKeyPrefix + version;
    }

    /**
     * 校验
     *
     * @param beanClass
     * @param routeKey
     */
    private void check(Class<?> beanClass, String routeKey) {
        // 是否已存在
        if (apiVersionBeanRoute.containsKey(routeKey)) {
            throw new DuplicateApiHandlerException(beanClass.getName());
        }
    }

    /**
     * 获取handler的method
     *
     * @param beanClass
     * @param apiHandlerVersion
     * @return
     */
    private Method getApiHandleMethod(Class<?> beanClass, ApiHandlerVersion apiHandlerVersion) {
        String methodName = apiHandlerVersion.method();
        Method[] apiHandlerMethods = beanClass.getMethods();

        // 优先ApiMethod注解匹配
        for (Method apiHandlerMethod : apiHandlerMethods) {
            if (AnnotationUtils.findAnnotation(apiHandlerMethod, ApiHandlerMethod.class) != null) {
                return apiHandlerMethod;
            }
        }

        // ApiHandlerVersion#method指定名字匹配
        for (Method apiHandlerMethod : apiHandlerMethods) {
            if (apiHandlerMethod.getName().equals(methodName)) {
                return apiHandlerMethod;
            }
        }

        throw new ApiHandlerMethodMissingException(beanClass.getName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}