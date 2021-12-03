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
package org.smartframework.cloud.starter.core.business.util;

import lombok.experimental.UtilityClass;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.springframework.context.annotation.Condition;

import java.util.Set;

/**
 * {@link Condition}工具类
 *
 * @author liyulin
 * @date 2019-04-27
 */
@UtilityClass
public class ReflectionUtil extends ReflectionUtils {

    private static Reflections reflections = null;

    static {
        reflections = new Reflections(PackageConfig.getBasePackages());
    }

    /**
     * 根据父类类型获取所有子类类型
     *
     * @param type
     * @return
     */
    public static <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return reflections.getSubTypesOf(type);
    }

}