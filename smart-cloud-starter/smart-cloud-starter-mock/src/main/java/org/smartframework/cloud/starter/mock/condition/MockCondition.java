package org.smartframework.cloud.starter.mock.condition;

import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 判断注解{@link SmartBootApplication}是否生效
 *
 * @author liyulin
 * @date 2019-04-27
 */
public class MockCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        Boolean mockApi = environment.getProperty(SmartConstant.MOCK_API_CONDITION_PROPERTY, Boolean.class);
        Boolean mockMethod = environment.getProperty(SmartConstant.MOCK_METHOD_CONDITION_PROPERTY, Boolean.class);
        return (mockApi != null && mockApi) || (mockMethod != null && mockMethod);
    }

}