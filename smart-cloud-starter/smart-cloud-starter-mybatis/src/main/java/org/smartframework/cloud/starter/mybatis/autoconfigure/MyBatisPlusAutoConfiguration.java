package org.smartframework.cloud.starter.mybatis.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.smartframework.cloud.starter.mybatis.plugin.MybatisSqlLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus配置
 *
 * @author liyulin
 * @date 2020-09-28
 */
@Configuration
public class MyBatisPlusAutoConfiguration {

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PaginationInnerInterceptor.class)
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "smart.cloud.mybatis.log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MybatisSqlLogInterceptor mybatisSqlLogInterceptor() {
        return new MybatisSqlLogInterceptor();
    }

}