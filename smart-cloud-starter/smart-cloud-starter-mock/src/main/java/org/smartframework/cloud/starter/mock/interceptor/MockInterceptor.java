package org.smartframework.cloud.starter.mock.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.starter.mock.util.MockUtil;

/**
 * mock切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
public class MockInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method m = invocation.getMethod();
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