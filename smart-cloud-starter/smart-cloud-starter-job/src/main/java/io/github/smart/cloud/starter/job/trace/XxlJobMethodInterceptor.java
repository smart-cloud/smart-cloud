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
package io.github.smart.cloud.starter.job.trace;

import com.xxl.job.core.handler.IJobHandler;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * xxl-job拦截器
 *
 * @param <T>
 * @author collin
 * @date 2021-04-11
 */
@RequiredArgsConstructor
public class XxlJobMethodInterceptor<T extends IJobHandler> implements MethodInterceptor {

    private final T delegate;
    private final BeanFactory beanFactory;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        IJobHandler jobHandler = new XxlJobTraceWrapper(beanFactory, delegate);
        Method methodOnTracedBean = getMethod(invocation, jobHandler);
        if (methodOnTracedBean != null) {
            try {
                return methodOnTracedBean.invoke(jobHandler, invocation.getArguments());
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                throw (cause != null) ? cause : ex;
            }
        }
        return invocation.proceed();
    }

    private Method getMethod(MethodInvocation invocation, Object object) {
        Method method = invocation.getMethod();
        return ReflectionUtils.findMethod(object.getClass(), method.getName(), method.getParameterTypes());
    }

}