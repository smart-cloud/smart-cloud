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
package io.github.smart.cloud.starter.web.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 异常处理策略工厂
 *
 * @author collin
 * @date 2021-11-13
 */
@Slf4j
@UtilityClass
public class ExceptionHandlerStrategyFactory {

    private static Set<IExceptionHandlerStrategy> EXCEPTION_HANDLER_STRATEGIES = null;

    static {
        Reflections reflections = new Reflections(IExceptionHandlerStrategy.class.getPackage().getName());
        Set<Class<? extends IExceptionHandlerStrategy>> exceptionHandlerStrategySet = reflections
                .getSubTypesOf(IExceptionHandlerStrategy.class);
        if (CollectionUtils.isNotEmpty(exceptionHandlerStrategySet)) {
            final String servletClassName = "javax.servlet.ServletException";
            boolean isServletEnv = ClassUtils.isPresent(servletClassName, ExceptionHandlerStrategyFactory.class.getClassLoader());
            Set<IExceptionHandlerStrategy> exceptionHandlerStrategies = new HashSet<>(exceptionHandlerStrategySet.size());
            for (Class<?> c : exceptionHandlerStrategySet) {
                try {
                    IExceptionHandlerStrategy exceptionHandlerStrategy = (IExceptionHandlerStrategy) c.newInstance();
                    if (!isServletEnv && exceptionHandlerStrategy.isNeedServletEnv()) {
                        continue;
                    }
                    exceptionHandlerStrategies.add(exceptionHandlerStrategy);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("IExceptionHandlerStrategy newInstance error", e);
                }
            }

            EXCEPTION_HANDLER_STRATEGIES = Collections.unmodifiableSet(exceptionHandlerStrategies);
        } else {
            EXCEPTION_HANDLER_STRATEGIES = Collections.unmodifiableSet(new HashSet<>(0));
        }
    }

    /**
     * 获取异常处理策略
     *
     * @return
     */
    public static Set<IExceptionHandlerStrategy> getExceptionHandlerStrategys() {
        return EXCEPTION_HANDLER_STRATEGIES;
    }

}