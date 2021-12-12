/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartframework.cloud.starter.mybatis.plus.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.mybatis.plus.injector.SmartSqlInjector;
import org.smartframework.cloud.starter.mybatis.plus.plugin.MybatisSqlLogInterceptor;
import org.smartframework.cloud.utility.spring.condition.ConditionEnableLogInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * mybatis plus配置
 *
 * @author collin
 * @date 2020-09-28
 */
@Configuration
public class MyBatisPlusAutoConfiguration {

    @Bean
    @Order(OrderConstant.MYBATIS_SQL_LOG_INTERCEPTOR)
    @Conditional(ConditionEnableLogInfo.class)
    @ConditionalOnProperty(prefix = "smart.cloud.mybatis.log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MybatisSqlLogInterceptor mybatisSqlLogInterceptor() {
        return new MybatisSqlLogInterceptor();
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    @Order(OrderConstant.MYBATIS_PLUS_INTERCEPTOR)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public SmartSqlInjector smartSqlInjector() {
        return new SmartSqlInjector();
    }

}