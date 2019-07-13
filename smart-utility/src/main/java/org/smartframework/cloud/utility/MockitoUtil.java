package org.smartframework.cloud.utility;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockCreationSettings;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ClassUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * mockito工具类
 * 
 * @author liyulin
 * @date 2019-06-17
 */
@Slf4j
@UtilityClass
public class MockitoUtil {

	private static final boolean SPRING_AOP_PRESENT = ClassUtils.isPresent(Advised.class.getTypeName(),
			ReflectionTestUtils.class.getClassLoader());
	@Getter
	private static ConcurrentLinkedQueue<MockDto> mockCache = new ConcurrentLinkedQueue<>();

	/**
	 * 设置mock属性
	 * 
	 * @param targetObject
	 * @param mockObject
	 * @param mockTypeEnum
	 */
	public static void setMockAttribute(Object targetObject, Object mockObject, MockTypeEnum mockTypeEnum) {
		if (SPRING_AOP_PRESENT) {
			targetObject = AopTestUtils.getUltimateTargetObject(targetObject);
		}
		Field[] fields = null;
		if (MockUtil.isSpy(targetObject) || MockUtil.isMock(targetObject)) {
			MockCreationSettings<?> targetObjectMockCreationSettings = MockUtil.getMockSettings(targetObject);
			Object spyObject = targetObjectMockCreationSettings.getSpiedInstance();
			fields = spyObject.getClass().getDeclaredFields();
		} else {
			fields = targetObject.getClass().getDeclaredFields();
		}
		
		if (fields == null) {
			return;
		}
		if (mockTypeEnum == MockTypeEnum.MOCK_BORROW) {
			MockCreationSettings<?> mockCreationSettings = MockUtil.getMockSettings(mockObject);
			Class<?> typeToMock = mockCreationSettings.getTypeToMock();
			mockCache.add(new MockDto(targetObject, typeToMock));
		}
		try {
			for (Field field : fields) {
				if (Object.class != field.getType() && field.getType().isInstance(mockObject)) {
					field.setAccessible(true);
					field.set(targetObject, mockObject);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 归还mock属性
	 * 
	 * @param applicationContext
	 */
	public static void revertMockAttribute(ApplicationContext applicationContext) {
		MockDto mockDto = null;
		while ((mockDto = MockitoUtil.getMockCache().poll()) != null) {
			MockitoUtil.setMockAttribute(mockDto.getTargetObject(), applicationContext.getBean(mockDto.getMockClass()),
					MockitoUtil.MockTypeEnum.MOCK_REVERT);
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class MockDto {
		Object targetObject;
		Class<?> mockClass;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public enum MockTypeEnum {
		/** 借 */
		MOCK_BORROW(1),
		/** 重置 */
		MOCK_REVERT(2);
		private int type;
	}

}