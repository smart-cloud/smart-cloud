package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import org.smartframework.cloud.starter.rpc.feign.interceptor.FeignSessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignSessionAutoConfiguration {

    @Bean
    public FeignSessionInterceptor feignSessionInterceptor() {
        return new FeignSessionInterceptor();
    }

}