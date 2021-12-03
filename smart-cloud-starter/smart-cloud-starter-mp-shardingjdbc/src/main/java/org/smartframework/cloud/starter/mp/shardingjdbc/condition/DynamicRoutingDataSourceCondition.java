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
package org.smartframework.cloud.starter.mp.shardingjdbc.condition;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 动态数据源生效条件
 *
 * @author collin
 * @date 2021-11-13
 */
public class DynamicRoutingDataSourceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        BindResult<DynamicDataSourceProperties> dynamicDataSourcePropertiesBindResult = Binder.get(context.getEnvironment())
                .bind(DynamicDataSourceProperties.PREFIX, DynamicDataSourceProperties.class);
        return dynamicDataSourcePropertiesBindResult.isBound() && MapUtils.isNotEmpty(dynamicDataSourcePropertiesBindResult.get().getDatasource());
    }

}