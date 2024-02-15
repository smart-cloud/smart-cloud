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

import io.github.smart.cloud.utility.spring.SpringContextUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 异常处理策略工厂
 *
 * @author collin
 * @date 2021-11-13
 */
@Slf4j
public class ExceptionHandlerStrategyFactory implements SmartInitializingSingleton {

    /**
     * 异常处理策略
     */
    @Getter
    private Set<IExceptionHandlerStrategy> exceptionHandlerStrategies = new HashSet<>();

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, IExceptionHandlerStrategy> exceptionHandlerStrategyBeanMap = SpringContextUtil.getApplicationContext().getBeansOfType(IExceptionHandlerStrategy.class);
        if (MapUtils.isEmpty(exceptionHandlerStrategyBeanMap)) {
            return;
        }

        exceptionHandlerStrategyBeanMap.values().forEach(exceptionHandlerStrategies::add);
    }

}