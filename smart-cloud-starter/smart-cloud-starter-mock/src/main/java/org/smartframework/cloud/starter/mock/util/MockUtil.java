package org.smartframework.cloud.starter.mock.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.experimental.UtilityClass;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * mock工具类
 *
 * @author liyulin
 * @date 2019-05-11
 */
@UtilityClass
public class MockUtil {

	private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

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