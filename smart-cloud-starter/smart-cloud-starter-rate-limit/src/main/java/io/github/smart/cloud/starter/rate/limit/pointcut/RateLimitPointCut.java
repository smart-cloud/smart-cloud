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
package io.github.smart.cloud.starter.rate.limit.pointcut;

import io.github.smart.cloud.starter.rate.limit.annotation.RateLimiter;
import io.github.smart.cloud.starter.rate.limit.properties.RateLimitProperties;
import io.github.smart.cloud.starter.rate.limit.util.RateLimitUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 限流切面
 *
 * @author collin
 * @date 2024-02-28
 */
@RequiredArgsConstructor
public class RateLimitPointCut extends DynamicMethodMatcherPointcut {

    private final RateLimitProperties rateLimitProperties;

    @Override
    public ClassFilter getClassFilter() {
        return c -> c.isAnnotationPresent(Controller.class)
                || c.isAnnotationPresent(RestController.class)
                || c.isAnnotationPresent(Service.class);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return method.isAnnotationPresent(RequestMapping.class)
                || method.isAnnotationPresent(GetMapping.class)
                || method.isAnnotationPresent(PostMapping.class)
                || method.isAnnotationPresent(PutMapping.class)
                || method.isAnnotationPresent(PatchMapping.class)
                || method.isAnnotationPresent(DeleteMapping.class)
                || method.isAnnotationPresent(RateLimiter.class);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        if (method.isAnnotationPresent(RateLimiter.class)) {
            return true;
        }

        Map<String, Integer> config = rateLimitProperties.getConfig();
        String semaphoreBeanName = RateLimitUtil.getSemaphoreBeanName(method);
        return config.containsKey(semaphoreBeanName);
    }

}