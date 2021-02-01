package org.smartframework.cloud.starter.core.test.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.core.support.annotation.SmartSpringCloudApplication;
import org.smartframework.cloud.starter.core.support.condition.SmartSpringCloudApplicationCondition;
import org.springframework.core.type.AnnotationMetadata;

public class SmartSpringCloudApplicationConditionUnitTest {

    @Test
    public void test() {
        SmartSpringCloudApplicationCondition smartSpringCloudApplicationCondition = new SmartSpringCloudApplicationCondition();
        AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(ApplicationTest.class);
        boolean result = smartSpringCloudApplicationCondition.matches(null, annotationMetadata);
        Assertions.assertTrue(result);
    }

    @SmartSpringCloudApplication
    public class ApplicationTest {

    }

}