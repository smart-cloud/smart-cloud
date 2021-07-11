package org.smartframework.cloud.starter.core.test.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.smartframework.cloud.starter.core.support.condition.SmartBootApplicationCondition;
import org.springframework.core.type.AnnotationMetadata;

class SmartSpringCloudApplicationConditionUnitTest {

    @Test
    void test() {
        SmartBootApplicationCondition smartBootApplicationCondition = new SmartBootApplicationCondition();
        AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(ApplicationTest.class);
        boolean result = smartBootApplicationCondition.matches(null, annotationMetadata);
        Assertions.assertTrue(result);
    }

    @SmartBootApplication
    public class ApplicationTest {

    }

}