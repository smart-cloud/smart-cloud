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
package io.github.smart.cloud.starter.rate.limit.util;

import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.starter.rate.limit.annotation.RateLimit;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 限流工具类
 *
 * @author collin
 * @date 2023-03-19
 */
public class RateLimitUtil {

    private RateLimitUtil() {
    }

    /**
     * 获取限流bean name
     *
     * @param method
     * @param rateLimit
     * @return
     */
    public static String getSemaphoreBeanName(Method method, RateLimit rateLimit) {
        String rateLimitBeanName = rateLimit.name();
        if (StringUtils.isBlank(rateLimitBeanName)) {
            rateLimitBeanName = method.getDeclaringClass().getName() + SymbolConstant.DOT + method.getName();
        }
        return rateLimitBeanName;
    }

}