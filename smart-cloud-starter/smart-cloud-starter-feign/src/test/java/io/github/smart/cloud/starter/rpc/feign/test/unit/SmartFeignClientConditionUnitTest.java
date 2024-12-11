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
package io.github.smart.cloud.starter.rpc.feign.test.unit;

import io.github.smart.cloud.starter.rpc.feign.condition.SmartFeignClientCondition;
import io.github.smart.cloud.starter.rpc.feign.test.SuiteTest;
import io.github.smart.cloud.starter.rpc.feign.test.prepare.Application;
import io.github.smart.cloud.starter.rpc.feign.test.prepare.rpc.TestRpc3;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.starter.core.constants.SmartApplicationConfig;
import io.github.smart.cloud.starter.rpc.feign.test.prepare.rpc.TestRpc1;
import io.github.smart.cloud.starter.rpc.feign.test.prepare.rpc.TestRpc2;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class SmartFeignClientConditionUnitTest {

    @Test
    void test() {
        SmartApplicationConfig.setBasePackages(new String[]{SuiteTest.class.getPackage().getName()});
        SmartFeignClientCondition smartFeignClientCondition = new SmartFeignClientCondition();

        AnnotationMetadata annotationMetadata1 = AnnotationMetadata.introspect(TestRpc1.class);
        boolean result1 = smartFeignClientCondition.matches(null, annotationMetadata1);
        Assertions.assertThat(result1).isTrue();

        AnnotationMetadata annotationMetadata2 = AnnotationMetadata.introspect(TestRpc2.class);
        boolean result2 = smartFeignClientCondition.matches(null, annotationMetadata2);
        Assertions.assertThat(result2).isFalse();

        AnnotationMetadata annotationMetadata3 = AnnotationMetadata.introspect(TestRpc3.class);
        boolean result3 = smartFeignClientCondition.matches(null, annotationMetadata3);
        Assertions.assertThat(result3).isFalse();
    }

}