package org.smartframework.cloud.starter.web.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@UtilityClass
public class ExceptionHandlerStrategyFactory {

    private static final Set<AbstractExceptionHandlerStrategy> EXCEPTION_HANDLER_STRATEGIES = new HashSet<>();

    static {
        Reflections reflections = new Reflections(AbstractExceptionHandlerStrategy.class.getPackage().getName());
        Set<Class<? extends AbstractExceptionHandlerStrategy>> exceptionHandlerStrategySet = reflections
                .getSubTypesOf(AbstractExceptionHandlerStrategy.class);
        if (CollectionUtils.isNotEmpty(exceptionHandlerStrategySet)) {
            final String servletClassName = "javax.servlet.ServletException";
            boolean isServletEnv = ClassUtils.isPresent(servletClassName, ExceptionHandlerStrategyFactory.class.getClassLoader());
            for (Class<?> c : exceptionHandlerStrategySet) {
                try {
                    AbstractExceptionHandlerStrategy exceptionHandlerStrategy = (AbstractExceptionHandlerStrategy) c.newInstance();
                    if (!isServletEnv && exceptionHandlerStrategy.isNeedServletEnv()) {
                        continue;
                    }
                    EXCEPTION_HANDLER_STRATEGIES.add(exceptionHandlerStrategy);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("IExceptionHandlerStrategy newInstance error", e);
                }
            }
        }
    }

    public static Set<AbstractExceptionHandlerStrategy> getExceptionHandlerStrategys() {
        return EXCEPTION_HANDLER_STRATEGIES;
    }

}