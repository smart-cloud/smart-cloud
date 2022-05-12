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
package io.github.smart.cloud.starter.core.method.log.intercept;

import io.github.smart.cloud.constants.LogLevel;
import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.mask.util.LogUtil;
import io.github.smart.cloud.mask.util.MaskUtil;
import io.github.smart.cloud.starter.configure.properties.LogProperties;
import io.github.smart.cloud.starter.core.method.log.annotation.MethodLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * method日志打印切面
 *
 * @author collin
 * @date 2021-03-13
 */
@Slf4j
@RequiredArgsConstructor
public class MethodLogInterceptor implements MethodInterceptor {

    private final LogProperties logProperties;
    /**
     * 慢日志
     */
    private static final String SLOW_LOG_PATTERN = "method.slow=>{}({}ms)-->args={}, result={}";
    /**
     * 普通日志
     */
    private static final String LOG_PATTERN = "method.log=>{}({}ms)-->args={}, result={}";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            if (log.isWarnEnabled()) {
                if (cost >= logProperties.getSlowApiMinCost()) {
                    String mastResult = (result instanceof String) ? (String) result : MaskUtil.mask(result);
                    log.warn(SLOW_LOG_PATTERN, getTag(invocation.getMethod()), cost, LogUtil.truncate(MaskUtil.mask(invocation.getArguments())), LogUtil.truncate(mastResult));
                } else {
                    MethodLog methodLog = invocation.getMethod().getAnnotation(MethodLog.class);
                    String logLevel = methodLog.level();
                    if (LogLevel.DEBUG.equals(logLevel) && log.isDebugEnabled()) {
                        log.debug(LOG_PATTERN, getTag(invocation.getMethod()), cost, getArgs(invocation.getArguments()), getResult(result));
                    } else if (LogLevel.INFO.equals(logLevel) && log.isInfoEnabled()) {
                        log.info(LOG_PATTERN, getTag(invocation.getMethod()), cost, getArgs(invocation.getArguments()), getResult(result));
                    } else if (LogLevel.WARN.equals(logLevel)) {
                        log.warn(LOG_PATTERN, getTag(invocation.getMethod()), cost, getArgs(invocation.getArguments()), getResult(result));
                    }
                }
            }
        }
        return result;
    }

    private String getTag(Method method) {
        return method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
    }

    /**
     * 获取参数
     *
     * @param arguments
     * @return
     */
    private final String getArgs(Object[] arguments) {
        return LogUtil.truncate(MaskUtil.mask(arguments));
    }

    /**
     * 获取返回结果
     *
     * @param result
     * @return
     */
    private final String getResult(Object result) {
        String mastResult = (result instanceof String) ? (String) result : MaskUtil.mask(result);
        return LogUtil.truncate(mastResult);
    }

}