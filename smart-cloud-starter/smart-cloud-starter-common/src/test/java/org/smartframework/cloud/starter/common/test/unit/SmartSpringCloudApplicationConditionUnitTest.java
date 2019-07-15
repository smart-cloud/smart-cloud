package org.smartframework.cloud.starter.common.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.starter.common.support.annotation.SmartSpringCloudApplication;
import org.smartframework.cloud.starter.common.support.condition.SmartSpringCloudApplicationCondition;
import org.springframework.core.type.StandardAnnotationMetadata;

import junit.framework.TestCase;

public class SmartSpringCloudApplicationConditionUnitTest extends TestCase {

	public void test() {
		SmartSpringCloudApplicationCondition smartSpringCloudApplicationCondition = new SmartSpringCloudApplicationCondition();
		StandardAnnotationMetadata standardAnnotationMetadata = new StandardAnnotationMetadata(ApplicationTest.class);
		boolean result = smartSpringCloudApplicationCondition.matches(null, standardAnnotationMetadata);
		Assertions.assertThat(result).isTrue();
	}

	@SmartSpringCloudApplication
	public class ApplicationTest {

	}

}