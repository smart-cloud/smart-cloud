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
package org.smartframework.cloud.starter.mock.interceptor;

import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.constants.SymbolConstant;
import org.smartframework.cloud.starter.configure.properties.MockProperties;
import org.smartframework.cloud.starter.mock.util.MockUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * mock切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
@AllArgsConstructor
public class MockInterceptor implements MethodInterceptor {

    private MockProperties mockProperties;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 白名单的直接跳过
        Method m = invocation.getMethod();
        Set<String> whitelist = mockProperties.getWhilelist();
        if (whitelist != null) {
            String code = m.getDeclaringClass().getName() + SymbolConstant.DOT + m.getName();
            if (whitelist.contains(code)) {
                return invocation.proceed();
            }
        }

        // 非白名单的返回mock
        Type returnType = m.getGenericReturnType();
        // 泛型
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return MockUtil.mock(m.getReturnType(), actualTypeArguments);
        } else {
            return MockUtil.mock(m.getReturnType());
        }
    }

}