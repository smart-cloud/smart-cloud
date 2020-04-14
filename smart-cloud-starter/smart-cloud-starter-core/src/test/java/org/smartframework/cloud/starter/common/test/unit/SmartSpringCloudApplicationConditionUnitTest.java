package org.smartframework.cloud.starter.common.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.starter.core.support.annotation.SmartSpringCloudApplication;
import org.smartframework.cloud.starter.core.support.condition.SmartSpringCloudApplicationCondition;
import org.springframework.core.type.AnnotationMetadata;

import junit.framework.TestCase;

public class SmartSpringCloudApplicationConditionUnitTest extends TestCase {

	public void test() {
		SmartSpringCloudApplicationCondition smartSpringCloudApplicationCondition = new SmartSpringCloudApplicationCondition();
		AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(ApplicationTest.class);
		boolean result = smartSpringCloudApplicationCondition.matches(null, annotationMetadata);
		Assertions.assertThat(result).isTrue();
	}

	@SmartSpringCloudApplication
	public class ApplicationTest {

	}

}