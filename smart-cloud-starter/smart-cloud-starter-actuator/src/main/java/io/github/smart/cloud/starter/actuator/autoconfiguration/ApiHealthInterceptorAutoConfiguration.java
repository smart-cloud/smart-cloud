package io.github.smart.cloud.starter.actuator.autoconfiguration;

import io.github.smart.cloud.starter.actuator.interceptor.ApiHealthInterceptor;
import io.github.smart.cloud.starter.actuator.pointcut.ApiHealthPointCut;
import io.github.smart.cloud.starter.actuator.properties.HealthProperties;
import io.github.smart.cloud.starter.actuator.repository.ApiHealthRepository;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口健康检测拦截器配置
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
public class ApiHealthInterceptorAutoConfiguration {

    @Bean
    public ApiHealthRepository apiHealthRepository(final HealthProperties healthProperties) {
        return new ApiHealthRepository(healthProperties);
    }

    @Bean
    public ApiHealthPointCut apiHealthPointCut() {
        return new ApiHealthPointCut();
    }

    @Bean
    public ApiHealthInterceptor apiHealthInterceptor(final ApiHealthRepository apiHealthRepository) {
        return new ApiHealthInterceptor(apiHealthRepository);
    }

    @Bean
    public Advisor apiHealthAdvisor(final ApiHealthInterceptor apiHealthInterceptor, final ApiHealthPointCut apiHealthPointCut) {
        DefaultBeanFactoryPointcutAdvisor apiHealthAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        apiHealthAdvisor.setAdvice(apiHealthInterceptor);
        apiHealthAdvisor.setPointcut(apiHealthPointCut);

        return apiHealthAdvisor;
    }

}