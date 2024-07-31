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

import de.codecentric.boot.admin.server.services.InstanceRegistry;
import io.github.smart.cloud.starter.monitor.admin.component.MetricsMonitorComponent;
import io.github.smart.cloud.starter.monitor.admin.component.metrics.*;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 服务指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Configuration
public class MetricsMonitorAutoConfiguration {

    @Bean
    @RefreshScope
    public CpuUsageMonitorComponent cpuUsageMonitorComponent() {
        return new CpuUsageMonitorComponent();
    }

    @Bean
    @RefreshScope
    public GcSpeedMonitorComponent gcSpeedMonitorComponent() {
        return new GcSpeedMonitorComponent();
    }

    @Bean
    @RefreshScope
    public JvmMemoryHeapUsedMonitorComponent jvmMemoryHeapUsedMonitorComponent() {
        return new JvmMemoryHeapUsedMonitorComponent();
    }

    @Bean
    @RefreshScope
    public JvmMemoryNonHeapUsedMonitorComponent jvmMemoryNonHeapUsedMonitorComponent() {
        return new JvmMemoryNonHeapUsedMonitorComponent();
    }

    @Bean
    @RefreshScope
    public LiveThreadCountMonitorComponent liveThreadCountMonitorComponent() {
        return new LiveThreadCountMonitorComponent();
    }

    @Bean
    @RefreshScope
    public MetricsMonitorComponent metricsMonitorComponent(final MonitorProperties monitorProperties, final InstanceRegistry instanceRegistry,
                                                           final List<IInstanceMetricsMonitorComponent> instanceMetricsMonitorComponents) {
        return new MetricsMonitorComponent(monitorProperties, instanceRegistry, instanceMetricsMonitorComponents);
    }

}