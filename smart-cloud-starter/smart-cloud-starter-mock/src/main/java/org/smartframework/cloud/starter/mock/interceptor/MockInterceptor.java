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
            String code = m.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + m.getName();
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