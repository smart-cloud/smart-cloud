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
package io.github.smart.cloud.starter.core.business.util;

import io.github.smart.cloud.starter.core.constants.PackageConfig;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Condition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Condition}工具类
 *
 * @author collin
 * @date 2019-04-27
 */
public class ReflectionUtil extends ReflectionUtils {

    private static Reflections reflections;

    static {
        String[] basePackages = PackageConfig.getBasePackages();
        Collection<URL> urls = new HashSet<>();
        for (String basePackage : basePackages) {
            urls.addAll(ClasspathHelper.forPackage(basePackage));
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .addUrls(urls)
                .addScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated, Scanners.SubTypes);
        reflections = new Reflections(configurationBuilder);
    }

    private ReflectionUtil() {
        super();
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

    /**
     * 获取类上有指定注解的类
     *
     * @param annotation
     * @return
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * 获取方法上有指定注解的方法
     *
     * @param annotation
     * @return
     */
    public static Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return reflections.getMethodsAnnotatedWith(annotation);
    }

}