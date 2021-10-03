package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import org.smartframework.cloud.starter.rpc.feign.interceptor.FeignHttpHeaderParameterInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHttpHeaderParameterInterceptorAutoConfiguration {

    @Bean
    public FeignHttpHeaderParameterInterceptor feignHttpHeaderParameterInterceptor() {
        return new FeignHttpHeaderParameterInterceptor();
    }

}