package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import org.smartframework.cloud.starter.rpc.feign.interceptor.FeignHttpHeaderParameterInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign client header参数拦截器配置
 *
 * @author collin
 * @date 2021-11-13
 */
@Configuration
public class FeignHttpHeaderParameterInterceptorAutoConfiguration {

    @Bean
    public FeignHttpHeaderParameterInterceptor feignHttpHeaderParameterInterceptor() {
        return new FeignHttpHeaderParameterInterceptor();
    }

}