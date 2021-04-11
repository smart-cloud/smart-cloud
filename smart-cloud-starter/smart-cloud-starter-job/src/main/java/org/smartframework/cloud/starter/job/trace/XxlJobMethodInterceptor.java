package org.smartframework.cloud.starter.job.trace;

import com.xxl.job.core.handler.IJobHandler;
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
public class XxlJobMethodInterceptor<T extends IJobHandler> implements MethodInterceptor {

    private final T delegate;
    private final BeanFactory beanFactory;

    public XxlJobMethodInterceptor(T delegate, BeanFactory beanFactory) {
        this.delegate = delegate;
        this.beanFactory = beanFactory;
    }

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