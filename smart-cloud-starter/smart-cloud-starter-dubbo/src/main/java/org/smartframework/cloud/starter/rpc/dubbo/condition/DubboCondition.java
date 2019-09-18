package org.smartframework.cloud.starter.rpc.dubbo.condition;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.smartframework.cloud.starter.common.business.util.ReflectionUtil;
import org.smartframework.cloud.utility.ObjectUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc dubbo rpc实例化条件判断
 * @author liyulin
 * @date 2019/09/18
 */
@Slf4j
public class DubboCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// 修饰类
		if (metadata instanceof AnnotationMetadataReadingVisitor) {
			return classMatch(metadata);
		} else if (metadata instanceof MethodMetadataReadingVisitor) {
			// 修饰方法
			return methodMatch(metadata);
		}

		throw new UnsupportedOperationException();
	}

	/**
	 * 条件注解 修饰class
	 * 
	 * @param metadata
	 * @return
	 */
	private boolean classMatch(AnnotatedTypeMetadata metadata) {
		AnnotationMetadataReadingVisitor classMetadata = (AnnotationMetadataReadingVisitor) metadata;
		String className = classMetadata.getClassName();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		}

		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getReturnType() != ReferenceBean.class) {
				continue;
			}
			if (matchWithMethod(method)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 条件注解 修饰method
	 * 
	 * @param metadata
	 * @return
	 */
	private boolean methodMatch(AnnotatedTypeMetadata metadata) {
		MethodMetadataReadingVisitor methodMetadata = (MethodMetadataReadingVisitor) metadata;
		String className = methodMetadata.getDeclaringClassName();
		Class<?> declaringClass = null;
		try {
			declaringClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		}

		Method method = null;
		try {
			method = declaringClass.getDeclaredMethod(methodMetadata.getMethodName());
		} catch (NoSuchMethodException | SecurityException e) {
			log.error(e.getMessage(), e);
		}

		return matchWithMethod(method);
	}

	private boolean matchWithMethod(Method method) {
		// 获取rpc类型
		Type type = method.getGenericReturnType();
		if (!(type instanceof ParameterizedType)) {
			throw new UnsupportedOperationException(String.format("获取不到%s的泛型值", type.getTypeName()));
		}
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type actualType = parameterizedType.getActualTypeArguments()[0];
		Class<?> rpcClass = null;
		try {
			rpcClass = Class.forName(actualType.getTypeName());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		}
		// 获取interface对应的所有实现类
		Set<?> subTypes = ReflectionUtil.getSubTypesOf(rpcClass);

		// 判断是否存在RPC interface的实现类，且实现类上有Service注解
		if (subTypes == null || subTypes.isEmpty()) {
			return true;
		}
		// 遍历bean
		for (Object subType : subTypes) {
			Class<?> subTypeClass = (Class<?>) subType;
			boolean isRpcImplementClass = isDubboRpcImplementClass(subTypeClass);
			if (isRpcImplementClass) {
				return false;
			}
		}

		return true;
	}

	private boolean isDubboRpcImplementClass(Class<?> clazz) {
		return ObjectUtil.isNotNull(AnnotationUtils.findAnnotation(clazz, Service.class));
	}

}