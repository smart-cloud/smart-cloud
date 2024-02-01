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
package io.github.smart.cloud.starter.monitor.autoconfigure;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import io.github.smart.cloud.starter.monitor.AppChangeNotifier;
import io.github.smart.cloud.starter.monitor.component.GitLabComponent;
import io.github.smart.cloud.starter.monitor.component.OfflineCheckComponent;
import io.github.smart.cloud.starter.monitor.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.listener.AppChangeWeworkNotice;
import io.github.smart.cloud.starter.monitor.listener.OfflineCheckListener;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 监控服务配置
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
@EnableAdminServer
public class MonitorAutoConfiguration {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "smart.monitor")
    public MonitorProperties monitorProperties() {
        return new MonitorProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public RobotComponent robotComponent(final MonitorProperties monitorProperties) {
        return new RobotComponent(monitorProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GitLabComponent gitLabComponent(final MonitorProperties monitorProperties) {
        return new GitLabComponent(monitorProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReminderComponent reminderComponent(final GitLabComponent gitLabComponent, final MonitorProperties monitorProperties) {
        return new ReminderComponent(gitLabComponent, monitorProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public OfflineCheckComponent reminderComponent(final InstanceRepository instanceRepository, final RobotComponent robotComponent, final MonitorProperties monitorProperties,
                                                   final ReminderComponent reminderComponent) {
        return new OfflineCheckComponent(instanceRepository, robotComponent, monitorProperties, reminderComponent);
    }

    @Bean
    @ConditionalOnMissingBean
    public AppChangeWeworkNotice appChangeWeworkNotice(final RobotComponent robotComponent, final ReminderComponent reminderComponent) {
        return new AppChangeWeworkNotice(robotComponent, reminderComponent);
    }

    @Bean
    @ConditionalOnMissingBean
    public OfflineCheckListener offlineCheckListener(final OfflineCheckComponent offlineCheckComponent) {
        return new OfflineCheckListener(offlineCheckComponent);
    }

    @Bean
    @ConditionalOnMissingBean
    public AppChangeNotifier appChangeNotifier(final InstanceRepository instanceRepository, final MonitorProperties monitorProperties, final ApplicationEventPublisher applicationEventPublisher) {
        return new AppChangeNotifier(instanceRepository, monitorProperties, applicationEventPublisher);
    }

}