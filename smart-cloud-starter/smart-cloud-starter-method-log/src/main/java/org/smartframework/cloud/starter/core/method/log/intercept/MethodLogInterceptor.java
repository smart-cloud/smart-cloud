package org.smartframework.cloud.starter.core.method.log.intercept;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.constants.SymbolConstant;
import org.smartframework.cloud.mask.util.LogUtil;
import org.smartframework.cloud.mask.util.MaskUtil;
import org.smartframework.cloud.starter.configure.properties.LogProperties;

import java.lang.reflect.Method;

/**
 * method日志打印切面
 *
 * @author collin
 * @date 2021-03-13
 */
@Slf4j
@AllArgsConstructor
public class MethodLogInterceptor implements MethodInterceptor {

    private LogProperties logProperties;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            if (cost >= logProperties.getSlowApiMinCost()) {
                String mastResult = (result instanceof String) ? (String) result : MaskUtil.mask(result);
                log.warn("method.slow=>{}({}ms)-->args={}, result={}", getTag(invocation.getMethod()), cost, LogUtil.truncate(MaskUtil.mask(invocation.getArguments())), LogUtil.truncate(mastResult));
            } else if (log.isInfoEnabled()) {
                String mastResult = (result instanceof String) ? (String) result : MaskUtil.mask(result);
                log.info("method.info=>{}({}ms)-->args={}, result={}", getTag(invocation.getMethod()), cost, LogUtil.truncate(MaskUtil.mask(invocation.getArguments())), LogUtil.truncate(mastResult));
            }
        }
        return result;
    }

    private String getTag(Method method) {
        return method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
    }

}