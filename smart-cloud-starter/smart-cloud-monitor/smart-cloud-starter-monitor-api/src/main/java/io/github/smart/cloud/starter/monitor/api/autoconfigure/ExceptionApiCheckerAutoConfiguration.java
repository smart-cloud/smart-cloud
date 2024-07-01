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
package io.github.smart.cloud.starter.monitor.api.autoconfigure;

import io.github.smart.cloud.starter.monitor.api.annotation.ConditionApiExceptionMonitor;
import io.github.smart.cloud.starter.monitor.api.component.ApiMonitorRepository;
import io.github.smart.cloud.starter.monitor.api.component.ExceptionApiChecker;
import io.github.smart.cloud.starter.monitor.api.listener.wework.ApiExceptionListener;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 异常接口检测配置
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
@ConditionApiExceptionMonitor
public class ExceptionApiCheckerAutoConfiguration {

    @Bean
    @RefreshScope
    public ExceptionApiChecker exceptionApiChecker(final ApiMonitorProperties apiMonitorProperties,
                                                   final ApiMonitorRepository apiMonitorRepository,
                                                   final ApplicationEventPublisher applicationEventPublisher) {
        return new ExceptionApiChecker(apiMonitorProperties, apiMonitorRepository, applicationEventPublisher);
    }

    @Bean
    @RefreshScope
    public ApiExceptionListener apiExceptionListener(final ApiMonitorProperties apiMonitorProperties) {
        return new ApiExceptionListener(apiMonitorProperties);
    }

}