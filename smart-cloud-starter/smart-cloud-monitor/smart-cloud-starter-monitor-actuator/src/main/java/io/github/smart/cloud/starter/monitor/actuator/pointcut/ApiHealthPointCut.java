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
package io.github.smart.cloud.starter.monitor.actuator.pointcut;

import io.github.smart.cloud.starter.monitor.actuator.annotation.ApiHealthMonitor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * 健康检测切面
 *
 * @author collin
 * @date 2022-07-12
 */
public class ApiHealthPointCut extends StaticMethodMatcherPointcut {
    /**
     * RestController注解类名
     */
    private static final String CONTROLLER_ANNOTATION_NAME = "org.springframework.stereotype.Controller";
    /**
     * RestController注解类名
     */
    private static final String REST_CONTROLLER_ANNOTATION_NAME = "org.springframework.web.bind.annotation.RestController";
    /**
     * FeignClient注解类名
     */
    private static final String FEIGN_CLIENT_ANNOTATION_NAME = "org.springframework.cloud.openfeign.FeignClient";
    /**
     * RequestMapping注解类名
     */
    private static final String REQUEST_MAPPING_ANNOTATION_NAME = "org.springframework.web.bind.annotation.RequestMapping";
    /**
     * GetMapping注解类名
     */
    private static final String GET_MAPPING_ANNOTATION_NAME = "org.springframework.web.bind.annotation.GetMapping";
    /**
     * PostMapping注解类名
     */
    private static final String POST_MAPPING_ANNOTATION_NAME = "org.springframework.web.bind.annotation.PostMapping";
    /**
     * DeleteMapping注解类名
     */
    private static final String DELETE_MAPPING_ANNOTATION_NAME = "org.springframework.web.bind.annotation.DeleteMapping";
    /**
     * PutMapping注解类名
     */
    private static final String PUT_MAPPING_ANNOTATION_NAME = "org.springframework.web.bind.annotation.PutMapping";
    /**
     * PatchMapping注解类名
     */
    private static final String PATCH_MAPPING_ANNOTATION_NAME = "org.springframework.web.bind.annotation.PatchMapping";

    @Override
    public boolean matches(Method method, Class<?> c) {
        if (AnnotatedElementUtils.isAnnotated(method, ApiHealthMonitor.class)) {
            return true;
        }

        return
                // 1.类
                // 1.1web接口
                (c != null && (AnnotatedElementUtils.isAnnotated(method.getDeclaringClass(), CONTROLLER_ANNOTATION_NAME)
                        || AnnotatedElementUtils.isAnnotated(method.getDeclaringClass(), REST_CONTROLLER_ANNOTATION_NAME)
                        // 1.2openfeign接口
                        || AnnotatedElementUtils.isAnnotated(method.getDeclaringClass(), FEIGN_CLIENT_ANNOTATION_NAME))) &&
                        // 2.方法
                        (AnnotatedElementUtils.isAnnotated(method, REQUEST_MAPPING_ANNOTATION_NAME)
                                || AnnotatedElementUtils.isAnnotated(method, GET_MAPPING_ANNOTATION_NAME)
                                || AnnotatedElementUtils.isAnnotated(method, POST_MAPPING_ANNOTATION_NAME)
                                || AnnotatedElementUtils.isAnnotated(method, DELETE_MAPPING_ANNOTATION_NAME)
                                || AnnotatedElementUtils.isAnnotated(method, PUT_MAPPING_ANNOTATION_NAME)
                                || AnnotatedElementUtils.isAnnotated(method, PATCH_MAPPING_ANNOTATION_NAME));
    }

}