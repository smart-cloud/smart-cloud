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
package org.smartframework.cloud.starter.rpc.dubbo.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.common.web.util.WebUtil;
import org.smartframework.cloud.constants.SymbolConstant;
import org.smartframework.cloud.mask.util.LogUtil;
import org.smartframework.cloud.starter.configure.properties.LogProperties;
import org.smartframework.cloud.starter.rpc.dubbo.pojo.DubboLogAspectDO;

import java.lang.reflect.Method;

/**
 * dubbo rpc接口日志切面
 *
 * @author collin
 * @date 2019-09-15
 */
@Slf4j
@AllArgsConstructor
public class DubboLogInterceptor implements MethodInterceptor {

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
                log.warn(LogUtil.truncate("rpc.slow=>{}", buildDubboLogAspectDO(startTime, invocation.getMethod(), invocation.getArguments(), result)));
            } else if (log.isInfoEnabled()) {
                log.info(LogUtil.truncate("rpc.info=>{}", buildDubboLogAspectDO(startTime, invocation.getMethod(), invocation.getArguments(), result)));
            }
        }

        return result;
    }

    private DubboLogAspectDO buildDubboLogAspectDO(long startTime, Method method, Object[] args, Object result) {
        DubboLogAspectDO logDO = new DubboLogAspectDO();
        logDO.setStartTime(startTime);

        String classMethod = method.getDeclaringClass().getTypeName() + SymbolConstant.DOT + method.getName();
        logDO.setClassMethod(classMethod);
        logDO.setParams(WebUtil.getRequestArgs(args));
        logDO.setEndTime(System.currentTimeMillis());
        logDO.setCost((int) (logDO.getEndTime() - logDO.getStartTime()));
        logDO.setResult(result);
        return logDO;
    }

}