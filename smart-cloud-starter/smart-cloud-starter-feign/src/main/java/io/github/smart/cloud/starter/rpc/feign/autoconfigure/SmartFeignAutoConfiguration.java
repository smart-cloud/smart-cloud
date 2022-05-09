package io.github.smart.cloud.starter.rpc.feign.autoconfigure;

import feign.Feign;
import feign.querymap.BeanQueryMapEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * openfeign配置
 *
 * @author collin
 * @date 2022-06-19
 */
@Configuration
public class SmartFeignAutoConfiguration {

    @Bean
    public Feign.Builder feignBuilder() {
        // 解决GET请求时，父类属性值丢失问题
        return Feign.builder().queryMapEncoder(new BeanQueryMapEncoder());
    }

}