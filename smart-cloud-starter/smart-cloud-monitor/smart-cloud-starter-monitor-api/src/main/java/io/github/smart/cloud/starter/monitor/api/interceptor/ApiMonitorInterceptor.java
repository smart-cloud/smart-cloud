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
package io.github.smart.cloud.starter.monitor.api.interceptor;

import io.github.smart.cloud.starter.monitor.api.component.ApiMonitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 接口健康监控
 *
 * @author collin
 * @date 2024-01-15
 */
@Slf4j
@RequiredArgsConstructor
public class ApiMonitorInterceptor implements MethodInterceptor {

    private final ApiMonitorRepository apiMonitorRepository;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String name = getApiName(invocation.getMethod());
        Object result = null;
        try {
            result = invocation.proceed();
            apiMonitorRepository.add(name, true, null);
        } catch (Throwable e) {
            apiMonitorRepository.add(name, false, e);
            throw e;
        }

        return result;
    }

    /**
     * 获取类标志符
     *
     * @param method
     * @return
     */
    private String getApiName(Method method) {
        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

}