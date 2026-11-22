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
package io.github.smart.cloud.starter.method.log.intercept;

import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.starter.configure.properties.MethodLogProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

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

    private final SmartProperties smartProperties;
    /**
     * 默认日志最大长度
     */
    private static final int DEFAULT_LOG_MAX_LENGTH = 2048;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodLogProperties methodLogProperties = smartProperties.getMethodLog();
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();

            long cost = System.currentTimeMillis() - startTime;
            if (cost >= methodLogProperties.getSlowApiMinCost()) {
                if (log.isWarnEnabled()) {
                    log.warn("method.slow=>{}({}ms)-->args={}, result={}", getTag(invocation.getMethod()), cost,
                            getArgs(invocation.getArguments(), methodLogProperties.getLogMaxLength()),
                            getResult(result, methodLogProperties.getLogMaxLength()));
                }
            } else if (log.isInfoEnabled()) {
                log.info("method.log=>{}({}ms)-->args={}, result={}", getTag(invocation.getMethod()), cost,
                        getArgs(invocation.getArguments(), methodLogProperties.getLogMaxLength()),
                        getResult(result, methodLogProperties.getLogMaxLength()));
            }
        } catch (Exception e) {
            long cost = System.currentTimeMillis() - startTime;
            log.error("method.error=>{}({}ms)-->args={}, result={}", getTag(invocation.getMethod()), cost,
                    getArgs(invocation.getArguments(), methodLogProperties.getLogMaxLength()),
                    getResult(result, methodLogProperties.getLogMaxLength()), e);
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
    private String getTag(Method method) {
        return method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
    }

    /**
     * 获取参数
     *
     * @param arguments
     * @param logMaxLength
     * @return
     */
    private String getArgs(Object[] arguments, Integer logMaxLength) {
        logMaxLength = (logMaxLength == null) ? DEFAULT_LOG_MAX_LENGTH : logMaxLength;
        return StringUtils.truncate(JacksonUtil.toJson(arguments), logMaxLength);
    }

    /**
     * 获取返回结果
     *
     * @param result
     * @param logMaxLength
     * @return
     */
    private String getResult(Object result, Integer logMaxLength) {
        String mastResult = (result instanceof String) ? (String) result : JacksonUtil.toJson(result);
        logMaxLength = (logMaxLength == null) ? DEFAULT_LOG_MAX_LENGTH : logMaxLength;
        return StringUtils.truncate(mastResult, logMaxLength);
    }

}