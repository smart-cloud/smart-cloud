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
package io.github.smart.cloud.starter.core.support.condition;

import io.github.smart.cloud.starter.core.constants.SmartEnv;
import io.github.smart.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;

/**
 * 判断注解{@link SmartBootApplication}是否生效
 *
 * @author collin
 * @date 2019-04-27
 */
public class SmartBootApplicationCondition implements Condition {

    private static String bootstrapClassName = null;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // unit test env不需要合并
        if (SmartEnv.isUnitTest()) {
            return true;
        }

        ClassMetadata classMetadata = (ClassMetadata) metadata;
        String currentClassName = classMetadata.getClassName();
        // 只有第一个被{@code SmartSpringCloudApplication}标记的类会生效
        if (bootstrapClassName == null) {
            bootstrapClassName = currentClassName;
            return true;
        }

        return bootstrapClassName.equals(currentClassName);
    }

}