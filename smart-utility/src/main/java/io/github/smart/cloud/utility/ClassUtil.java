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
package io.github.smart.cloud.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * class工具类
 *
 * @author collin
 * @date 2024-07-12
 */
public class ClassUtil {

    /**
     * 获取类属性的值
     *
     * @param instance 实例对象
     * @param clazz    类
     * @param name     属性名
     * @param <T>
     * @return
     */
    public static <T> T findFieldValue(Object instance, Class<?> clazz, String name) {
        return AccessController.doPrivileged((PrivilegedAction<T>) () -> {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(instance);
            } catch (ReflectiveOperationException | SecurityException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 查找类中的方法
     *
     * @param clazz          类
     * @param name           方法名
     * @param parameterTypes 方法参数类型列表
     * @return
     */
    public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return AccessController.doPrivileged((PrivilegedAction<Method>) () -> {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

}