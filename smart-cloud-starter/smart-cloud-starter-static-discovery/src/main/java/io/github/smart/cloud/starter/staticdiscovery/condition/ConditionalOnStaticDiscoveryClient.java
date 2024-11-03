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
package io.github.smart.cloud.starter.staticdiscovery.condition;

import io.github.smart.cloud.starter.staticdiscovery.properties.StaticDiscoveryProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 静态地址发现开启条件
 *
 * @author collin
 * @date 2024-11-03
 */
public class ConditionalOnStaticDiscoveryClient implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        BindResult<StaticDiscoveryProperties> bindResult = Binder.get(context.getEnvironment()).bind(StaticDiscoveryProperties.PREFIX, StaticDiscoveryProperties.class);
        if (!bindResult.isBound()) {
            return false;
        }

        StaticDiscoveryProperties staticDiscoveryProperties = bindResult.get();
        return staticDiscoveryProperties != null
                && staticDiscoveryProperties.getInstanceConfig() != null
                && !staticDiscoveryProperties.getInstanceConfig().isEmpty();
    }

}