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

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import io.github.smart.cloud.starter.monitor.admin.AppChangeNotifier;
import io.github.smart.cloud.starter.monitor.admin.component.GitLabComponent;
import io.github.smart.cloud.starter.monitor.admin.listener.OfflineCheckListener;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.admin.schedule.OfflineCheckSchedule;
import io.github.smart.cloud.starter.monitor.admin.schedule.ServiceNodeCountCheckSchedule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class MonitorAutoConfiguration {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "smart.monitor")
    public MonitorProperties monitorProperties() {
        return new MonitorProperties();
    }

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "smart.cloud.monitor", value = "gitlab.enabled", havingValue = "true", matchIfMissing = true)
    public GitLabComponent gitLabComponent(final MonitorProperties monitorProperties) {
        return new GitLabComponent(monitorProperties);
    }

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean
    public OfflineCheckSchedule offlineCheckSchedule(final InstanceRepository instanceRepository, final MonitorProperties monitorProperties,
                                                     final ApplicationEventPublisher applicationEventPublisher) {
        return new OfflineCheckSchedule(instanceRepository, monitorProperties, applicationEventPublisher);
    }

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean
    public ServiceNodeCountCheckSchedule serviceNodeCountCheckSchedule(final InstanceRepository instanceRepository, final MonitorProperties monitorProperties,
                                                                       final ApplicationEventPublisher applicationEventPublisher) {
        return new ServiceNodeCountCheckSchedule(instanceRepository, monitorProperties, applicationEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public OfflineCheckListener offlineCheckListener(final OfflineCheckSchedule offlineCheckSchedule) {
        return new OfflineCheckListener(offlineCheckSchedule);
    }

    @Bean
    @ConditionalOnMissingBean
    public AppChangeNotifier appChangeNotifier(final InstanceRepository instanceRepository, final MonitorProperties monitorProperties,
                                               final ApplicationEventPublisher applicationEventPublisher) {
        return new AppChangeNotifier(instanceRepository, monitorProperties, applicationEventPublisher);
    }

}