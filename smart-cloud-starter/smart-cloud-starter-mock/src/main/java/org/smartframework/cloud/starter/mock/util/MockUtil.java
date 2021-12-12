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
package org.smartframework.cloud.starter.mock.util;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * mock工具类
 *
 * @author collin
 * @date 2019-05-11
 */
public class MockUtil {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    private MockUtil() {
    }

    /**
     * mock对象
     *
     * @param pojoClass
     * @param genericTypeArgs
     * @return
     */
    public static <T> T mock(Class<T> pojoClass, Type... genericTypeArgs) {
        return PODAM_FACTORY.manufacturePojo(pojoClass, genericTypeArgs);
    }

    /**
     * mock对象，支持嵌套泛型
     *
     * @param typeReference
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T mock(TypeReference<T> typeReference) {
        Type type = typeReference.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<T> rawTypeClass = (Class<T>) (parameterizedType).getRawType();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return PODAM_FACTORY.manufacturePojo(rawTypeClass, actualTypeArguments);
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType instanceof ParameterizedType) {
                throw new UnsupportedOperationException("不支持泛型数组操作");
            } else {
                Class<T> clazz = (Class<T>) type;
                return PODAM_FACTORY.manufacturePojo(clazz);
            }
        } else if (type instanceof Class) {
            Class<T> clazz = (Class<T>) type;
            return PODAM_FACTORY.manufacturePojo(clazz);
        }
        throw new UnsupportedOperationException("不支持的类型（" + type.getTypeName() + "）操作");
    }

}