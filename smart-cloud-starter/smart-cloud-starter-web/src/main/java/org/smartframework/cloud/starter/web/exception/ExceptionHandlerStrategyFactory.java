package org.smartframework.cloud.starter.web.exception;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ExceptionHandlerStrategyFactory {

	private static final Set<IExceptionHandlerStrategy> EXCEPTION_HANDLER_STRATEGIES = new HashSet<>();

	static {
		Reflections reflections = new Reflections(IExceptionHandlerStrategy.class.getPackage().getName());
		Set<Class<? extends IExceptionHandlerStrategy>> exceptionHandlerStrategySet = reflections
				.getSubTypesOf(IExceptionHandlerStrategy.class);
		if (CollectionUtils.isNotEmpty(exceptionHandlerStrategySet)) {
			for (Class<?> c : exceptionHandlerStrategySet) {
				try {
					EXCEPTION_HANDLER_STRATEGIES.add((IExceptionHandlerStrategy) c.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("IExceptionHandlerStrategy newInstance error", e);
				}
			}
		}
	}
	
	public static Set<IExceptionHandlerStrategy> getExceptionHandlerStrategys(){
		return EXCEPTION_HANDLER_STRATEGIES;
	}

}