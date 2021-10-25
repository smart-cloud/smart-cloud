package org.smartframework.cloud.starter.core.method.log.intercept;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.mask.util.MaskUtil;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * method日志打印切面
 *
 * @author collin
 * @date 2021-03-13
 */
@Slf4j
public class MethodLogInterceptor implements MethodInterceptor {

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Method method = invocation.getMethod();

        String tag = method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
        Object[] args = invocation.getArguments();
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("method.log=>{}({}ms)-->args={}, result={}", tag, (endTime - startTime), MaskUtil.mask(args), MaskUtil.mask(result));
        }
        return result;
    }

}