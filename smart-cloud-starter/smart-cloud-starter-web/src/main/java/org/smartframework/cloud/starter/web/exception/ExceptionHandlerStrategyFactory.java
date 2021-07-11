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

    private static final Set<IExceptionHandlerStrategy> EXCEPTION_HANDLER_STRATEGIES = new HashSet<>();

    static {
        Reflections reflections = new Reflections(IExceptionHandlerStrategy.class.getPackage().getName());
        Set<Class<? extends IExceptionHandlerStrategy>> exceptionHandlerStrategySet = reflections
                .getSubTypesOf(IExceptionHandlerStrategy.class);
        if (CollectionUtils.isNotEmpty(exceptionHandlerStrategySet)) {
            final String servletClassName = "javax.servlet.ServletException";
            boolean isServletEnv = ClassUtils.isPresent(servletClassName, ExceptionHandlerStrategyFactory.class.getClassLoader());
            for (Class<?> c : exceptionHandlerStrategySet) {
                try {
                    IExceptionHandlerStrategy exceptionHandlerStrategy = (IExceptionHandlerStrategy) c.newInstance();
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

    public static Set<IExceptionHandlerStrategy> getExceptionHandlerStrategys() {
        return EXCEPTION_HANDLER_STRATEGIES;
    }

}