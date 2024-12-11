/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.core.test.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.smart.cloud.starter.core.annotation.SmartBootApplication;
import io.github.smart.cloud.starter.core.condition.SmartBootApplicationCondition;
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