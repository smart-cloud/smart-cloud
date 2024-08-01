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
package io.github.smart.cloud.starter.monitor.api.component;

import io.github.smart.cloud.starter.monitor.api.dto.ApiExceptionDTO;
import io.github.smart.cloud.starter.monitor.api.event.ApiExceptionNoticeEvent;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异常接口监控，企业微信机器人通知
 *
 * @author collin
 * @date 2024-04-28
 */
@Slf4j
@RequiredArgsConstructor
public class ExceptionApiChecker implements InitializingBean, DisposableBean, ApplicationListener<RefreshScopeRefreshedEvent> {

    private final ApiMonitorProperties apiMonitorProperties;
    private final ApiMonitorRepository apiRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private ScheduledExecutorService exceptionApiCheckSchedule;

    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionApiCheckSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("exception-api-notice-schedule"));
        exceptionApiCheckSchedule.scheduleWithFixedDelay(this::checkExceptionApiAndNotice, apiMonitorProperties.getApiExceptionNoticeIntervalSeconds(),
                apiMonitorProperties.getApiExceptionNoticeIntervalSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 检测异常接口，并发送通知
     */
    public void checkExceptionApiAndNotice() {
        List<ApiExceptionDTO> apiExceptions = apiRepository.getApiExceptions();
        if (apiExceptions.isEmpty()) {
            return;
        }

        applicationEventPublisher.publishEvent(new ApiExceptionNoticeEvent(this, apiExceptions));
    }

    @Override
    public void destroy() throws Exception {
        if (exceptionApiCheckSchedule != null) {
            exceptionApiCheckSchedule.shutdown();
        }
    }

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        // 处理“@RefreshScope会导致ScheduledExecutorService失效”的问题
        // do nothing
    }

}