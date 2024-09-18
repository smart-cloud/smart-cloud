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

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import io.github.smart.cloud.starter.monitor.admin.component.metrics.IInstanceMetricsMonitorComponent;
import io.github.smart.cloud.starter.monitor.admin.component.metrics.impl.*;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.admin.schedule.MetricsMonitorSchedule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.github.smart.cloud.starter.monitor.admin.autoconfigure.MetricsMonitorAutoConfiguration.MONITOR_METRICS_PREFIX;

/**
 * 服务指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Configuration
@ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class MetricsMonitorAutoConfiguration {

    /**
     * 监控指标前缀
     */
    public static final String MONITOR_METRICS_PREFIX = "smart.cloud.monitor.metrics";

    @Bean
    @RefreshScope
    @ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "cpu-usage.enabled", havingValue = "true", matchIfMissing = true)
    public CpuUsageMonitorComponent cpuUsageMonitorComponent() {
        return new CpuUsageMonitorComponent();
    }

    @Bean
    @RefreshScope
    @ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "gc-speed.enabled", havingValue = "true", matchIfMissing = true)
    public GcSpeedMonitorComponent gcSpeedMonitorComponent() {
        return new GcSpeedMonitorComponent();
    }

    @Bean
    @RefreshScope
    @ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "jvm-heap.enabled", havingValue = "true", matchIfMissing = true)
    public JvmMemoryHeapUsedMonitorComponent jvmMemoryHeapUsedMonitorComponent() {
        return new JvmMemoryHeapUsedMonitorComponent();
    }

    @Bean
    @RefreshScope
    @ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "jvm-nonheap.enabled", havingValue = "true", matchIfMissing = true)
    public JvmMemoryNonHeapUsedMonitorComponent jvmMemoryNonHeapUsedMonitorComponent() {
        return new JvmMemoryNonHeapUsedMonitorComponent();
    }

    @Bean
    @RefreshScope
    @ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "live-thread-count.enabled", havingValue = "true", matchIfMissing = true)
    public LiveThreadCountMonitorComponent liveThreadCountMonitorComponent() {
        return new LiveThreadCountMonitorComponent();
    }

    @Bean
    @RefreshScope
    @ConditionalOnProperty(prefix = MONITOR_METRICS_PREFIX, value = "tomcat.enabled", havingValue = "true", matchIfMissing = true)
    public IInstanceMetricsMonitorComponent tomcatMetricMonitorComponent(final ApplicationEventPublisher applicationEventPublisher,
                                                                         final ObjectMapper objectMapper,
                                                                         final MonitorProperties monitorProperties) {
        return new TomcatMetricMonitorComponent(applicationEventPublisher, objectMapper, monitorProperties);
    }

    @Bean
    @RefreshScope
    public MetricsMonitorSchedule metricsMonitorSchedule(final InstanceRegistry instanceRegistry,
                                                         final List<IInstanceMetricsMonitorComponent> instanceMetricsMonitorComponents) {
        return new MetricsMonitorSchedule(instanceRegistry, instanceMetricsMonitorComponents);
    }

}