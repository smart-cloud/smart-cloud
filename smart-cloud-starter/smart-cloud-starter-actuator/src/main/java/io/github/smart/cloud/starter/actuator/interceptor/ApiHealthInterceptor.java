package io.github.smart.cloud.starter.actuator.interceptor;

import io.github.smart.cloud.starter.actuator.repository.ApiHealthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * 接口健康监控
 *
 * @author collin
 * @date 2024-01-15
 */
@Slf4j
@RequiredArgsConstructor
public class ApiHealthInterceptor implements MethodInterceptor, Ordered {

    private final ApiHealthRepository apiHealthRepository;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        try {
            result = invocation.proceed();
            String name = getApiName(invocation.getMethod());
            try {
                apiHealthRepository.add(name, true);
            } catch (Exception e) {
                log.error("web api health info add error|tag={}", name, e);
            }
        } catch (Exception e) {
            apiHealthRepository.add(getApiName(invocation.getMethod()), false);
            throw e;
        }

        return result;
    }

    /**
     * 获取类标志符
     *
     * @param method
     * @return
     */
    private String getApiName(Method method) {
        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

}