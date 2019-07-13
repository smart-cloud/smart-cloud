package org.smartframework.cloud.starter.common.business.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.utility.ArrayUtil;
import org.smartframework.cloud.utility.ObjectUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.experimental.UtilityClass;

/**
 * 切面拦截器工具类
 *
 * @author liyulin
 * @date 2019-04-21
 */
@UtilityClass
public class AspectInterceptorUtil {

	/** 接口描述缓存 */
	private ConcurrentMap<String, String> apiDescMap = new ConcurrentHashMap<>();

	/**
	 * 获取feign method的描述
	 * 
	 * @param method
	 * @param path
	 * @return
	 */
	public String getFeignMethodDesc(Method method, String path) {
		// 先从缓存取
		String apiDesc = apiDescMap.get(path);
		if (ObjectUtil.isNotNull(apiDesc)) {
			return apiDesc;
		}

		// 缓存没有，则通过反射获取
		ApiOperation operation = method.getAnnotation(ApiOperation.class);
		apiDesc = ObjectUtil.isNotNull(operation) ? operation.value() : StringUtils.EMPTY;

		apiDescMap.putIfAbsent(path, apiDesc);

		return apiDesc;
	}

	/**
	 * 获取controller method的描述
	 * 
	 * @param method
	 * @param path
	 * @return
	 */
	public String getControllerMethodDesc(Method method, String path) {
		// 先从缓存取
		String apiDesc = apiDescMap.get(path);
		if (ObjectUtil.isNotNull(apiDesc)) {
			return apiDesc;
		}

		// 缓存没有，则通过反射获取
		ApiOperation operation = method.getAnnotation(ApiOperation.class);
		apiDesc = ObjectUtil.isNotNull(operation) ? operation.value() : StringUtils.EMPTY;
		if (StringUtils.isBlank(apiDesc)) {
			// 如果为空，则从接口类rpc取
			Class<?> controllerClass = method.getDeclaringClass();
			Class<?>[] interfaces = controllerClass.getInterfaces();
			if (ArrayUtil.isNotEmpty(interfaces)) {
				Class<?> rpcClass = interfaces[0];
				Method[] methods = rpcClass.getMethods();
				for (Method rpcMethod : methods) {
					if (isSameMethod(rpcMethod, method)) {
						operation = rpcMethod.getAnnotation(ApiOperation.class);
						apiDesc = ObjectUtil.isNotNull(operation) ? operation.value() : StringUtils.EMPTY;
						break;
					}
				}
			}
		}

		apiDescMap.putIfAbsent(path, apiDesc);

		return apiDesc;
	}

	/**
	 * 获取被注解标记的类切面表达式
	 * 
	 * @param annotations
	 * @return
	 */
	public static String getWithinExpression(List<Class<? extends Annotation>> annotations) {
		StringBuilder expression = new StringBuilder();
		for (int i = 0; i < annotations.size(); i++) {
			expression.append("@within(" + annotations.get(i).getTypeName() + ")");
			if (i != annotations.size() - 1) {
				expression.append(" || ");
			}
		}
		return expression.toString();
	}
	
	/**
	 * 获取接口切面表达式
	 * 
	 * @param basePackages
	 * @return
	 */
	public static String getApiExpression(String[] basePackages) {
		String controllerExpression = getWithinExpression(Arrays.asList(Controller.class, RestController.class));
		
		StringBuilder executions = new StringBuilder();
		for (int i = 0; i < basePackages.length; i++) {
			executions.append("execution( * " + basePackages[i] + "..*.*(..))");
			if (i != basePackages.length - 1) {
				executions.append(" || ");
			}
		}
		
		return "(" + executions + ") && (" + controllerExpression + ")";
	}

	/**
	 * 是否是同一个method
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean isSameMethod(Method a, Method b) {
		return (a.getReturnType() == b.getReturnType()) && ObjectUtil.equals(a.getName(), b.getName());
	}

}