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
package io.github.smart.cloud.starter.actuator.pointcut;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * 健康检测切面
 *
 * @author collin
 * @date 2022-07-12
 */
public class ApiHealthPointCut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> c) {
        return
                // 1.类
                // 1.1web接口
                (c != null && (c.isAnnotationPresent(RestController.class)
                        || c.isAnnotationPresent(Controller.class)
                        // 1.2openfeign接口
                        || c.isAnnotationPresent(FeignClient.class))) &&
                        // 2.方法
                        (AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class)
                                || AnnotatedElementUtils.hasAnnotation(method, GetMapping.class)
                                || AnnotatedElementUtils.hasAnnotation(method, PostMapping.class)
                                || AnnotatedElementUtils.hasAnnotation(method, DeleteMapping.class)
                                || AnnotatedElementUtils.hasAnnotation(method, PutMapping.class)
                                || AnnotatedElementUtils.hasAnnotation(method, PatchMapping.class));
    }

}