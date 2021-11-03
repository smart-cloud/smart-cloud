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