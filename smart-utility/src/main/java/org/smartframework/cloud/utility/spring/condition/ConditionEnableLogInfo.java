package org.smartframework.cloud.utility.spring.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * info级别日志生效条件注解
 *
 * @author collin
 * @date 2021-09-25
 */
@Slf4j
public class ConditionEnableLogInfo implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return log.isInfoEnabled();
    }

}