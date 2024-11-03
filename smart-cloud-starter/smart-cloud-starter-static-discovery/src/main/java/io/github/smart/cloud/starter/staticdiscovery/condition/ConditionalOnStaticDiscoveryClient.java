package io.github.smart.cloud.starter.staticdiscovery.condition;

import io.github.smart.cloud.starter.staticdiscovery.properties.StaticDiscoveryProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

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