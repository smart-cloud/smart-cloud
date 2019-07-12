package org.smartframework.cloud.starter.test.runner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.reflections.Reflections;
import org.smartframework.cloud.utility.ArrayUtil;
import org.springframework.core.annotation.AnnotationUtils;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * 套件测试Runner
 * 
 * <p>
 * 自动搜索满足条件的Test类，以便以套件的方式进行测试
 *
 * @author liyulin
 * @date 2019年4月28日上午12:54:15
 */
@Slf4j
public class AllTestsRunner extends Suite {

	private static final String TEST_CASE_PREFIX = "test";

	public AllTestsRunner(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
		super(builder, clazz, getSuiteClasses(clazz));
	}

	private static Class<?>[] getSuiteClasses(Class<?> bootstrapClazz) {
		String testCasePackage = bootstrapClazz.getPackage().getName();
		log.info("扫描package={}", testCasePackage);
		Reflections reflections = new Reflections(testCasePackage);
		
		Class<?>[] allSuperClass = { TestCase.class };
		Set<Class<?>> testClassSet = new HashSet<>();
		for (Class<?> superClass : allSuperClass) {
			testClassSet.addAll(reflections.getSubTypesOf(superClass));
		}
		
		Set<Class<?>> suiteClasses = testClassSet.stream()
				.filter(clazz -> !isAbstractClass(clazz) && isContainTestCase(clazz)).collect(Collectors.toSet());

		if (suiteClasses.isEmpty()) {
			log.warn("None suite test class is found!");
		}
		return suiteClasses.toArray(new Class<?>[suiteClasses.size()]);
	}

	/**
	 * 是否是抽象类
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean isAbstractClass(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getClass().getModifiers());
	}

	/**
	 * 是否包含test case
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean isContainTestCase(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		if (ArrayUtil.isEmpty(methods)) {
			return false;
		}
		if (TestCase.class.isAssignableFrom(clazz)) {
			for (Method method : methods) {
				if (method.getName().startsWith(TEST_CASE_PREFIX)) {
					return true;
				}
			}
		} else {
			// 所有的method中，至少有一个被@Test修饰的类
			for (Method method : methods) {
				if (AnnotationUtils.findAnnotation(method, Test.class) != null) {
					return true;
				}
			}
		}

		return false;
	}

}