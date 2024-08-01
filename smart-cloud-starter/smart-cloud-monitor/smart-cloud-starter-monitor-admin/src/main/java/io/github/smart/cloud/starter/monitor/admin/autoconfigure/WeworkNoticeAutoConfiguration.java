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
package io.github.smart.cloud.starter.monitor.admin.autoconfigure;

import io.github.smart.cloud.starter.monitor.admin.component.GitLabComponent;
import io.github.smart.cloud.starter.monitor.admin.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.admin.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.admin.listener.wework.AppChangeWeworkNotice;
import io.github.smart.cloud.starter.monitor.admin.listener.wework.MetricsAlertListener;
import io.github.smart.cloud.starter.monitor.admin.listener.wework.OfflineNotice;
import io.github.smart.cloud.starter.monitor.admin.listener.wework.ServiceNodeCountCheckNotice;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 企业微信通知配置
 *
 * @author collin
 * @date 2024-05-06
 */
@Configuration
@ConditionalOnProperty(name = "smart.monitor.wework.enable", havingValue = "true", matchIfMissing = true)
public class WeworkNoticeAutoConfiguration {

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean
    public RobotComponent robotComponent(final MonitorProperties monitorProperties) {
        return new RobotComponent(monitorProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReminderComponent reminderComponent(final GitLabComponent gitLabComponent, final MonitorProperties monitorProperties) {
        return new ReminderComponent(gitLabComponent, monitorProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public OfflineNotice offlineWeworkNotice(final RobotComponent robotComponent, final MonitorProperties monitorProperties, final ReminderComponent reminderComponent) {
        return new OfflineNotice(robotComponent, monitorProperties, reminderComponent);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceNodeCountCheckNotice serviceNodeCountCheckNotice(final RobotComponent robotComponent, final MonitorProperties monitorProperties, final ReminderComponent reminderComponent) {
        return new ServiceNodeCountCheckNotice(robotComponent, monitorProperties, reminderComponent);
    }

    @Bean
    @ConditionalOnMissingBean
    public AppChangeWeworkNotice appChangeWeworkNotice(final RobotComponent robotComponent, final ReminderComponent reminderComponent) {
        return new AppChangeWeworkNotice(robotComponent, reminderComponent);
    }

    @Bean
    public MetricsAlertListener metricsAlertListener(final RobotComponent robotComponent) {
        return new MetricsAlertListener(robotComponent);
    }

}