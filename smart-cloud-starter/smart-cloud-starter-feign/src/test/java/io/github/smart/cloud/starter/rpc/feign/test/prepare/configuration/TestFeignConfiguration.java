package io.github.smart.cloud.starter.rpc.feign.test.prepare.configuration;

import io.github.smart.cloud.starter.rpc.feign.test.prepare.interceptor.TestRequestInterceptor;
import org.springframework.context.annotation.Bean;

public class TestFeignConfiguration {

    @Bean
    public TestRequestInterceptor testRequestInterceptor() {
        return new TestRequestInterceptor();
    }

}