package org.smartframework.cloud.starter.mybatis.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.experimental.UtilityClass;

/**
 * Class工具类
 *
 * @author liyulin
 * @date 2019-03-30
 */
@UtilityClass
public class ClassUtil {

	/**
	 * 获取interface的泛型Class对象
	 * 
	 * @param clazz
	 * @param index 泛型所在的索引位置
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> getActualTypeArgumentFromSuperGenericInterface(Class<?> clazz, int index) {
		Type[] subInterfaceTypes = clazz.getGenericInterfaces();
		Type subInterfaceType = subInterfaceTypes[0];
		Class<?> subInterface = (Class<?>) subInterfaceType;
		Type[] superInterfaceTypes = subInterface.getGenericInterfaces();

		ParameterizedType superInterfaceType = (ParameterizedType) superInterfaceTypes[0];
		return (Class<T>) superInterfaceType.getActualTypeArguments()[index];
	}

	/**
	 * 获取类的泛型Class对象
	 * 
	 * @param clazz
	 * @param index 泛型所在的索引位置
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> getActualTypeArgumentFromSuperGenericClass(Class<?> clazz, int index) {
		Type type = clazz.getGenericSuperclass();

		ParameterizedType superClassType = (ParameterizedType) type;
		return (Class<T>) superClassType.getActualTypeArguments()[index];
	}

}