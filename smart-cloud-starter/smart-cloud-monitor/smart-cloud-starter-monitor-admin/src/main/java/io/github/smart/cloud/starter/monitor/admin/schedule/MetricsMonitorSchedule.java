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
package io.github.smart.cloud.starter.monitor.admin.schedule;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.Endpoints;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import de.codecentric.boot.admin.server.web.ApplicationsController;
import de.codecentric.boot.admin.server.web.servlet.InstancesProxyController;
import io.github.smart.cloud.starter.monitor.admin.component.metrics.IInstanceMetricsMonitorComponent;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务实例指标监控
 *
 * <ul>
 *     <li>cpu使用率监控（超过阈值）</li>
 *     <li>gc次数监控（在指定时间内，gc次数超过阈值）</li>
 *     <li>线程数监控（超过阈值）</li>
 *     <li>内存使用监控（超过阈值；总体一直增长(内存泄漏)）</li>
 * </ul>
 *
 * @author collin
 * @date 2024-07-23
 * @see ApplicationsController#applications()
 * @see InstancesProxyController#endpointProxy(String, HttpServletRequest, HttpServletResponse)
 */
@Slf4j
@RequiredArgsConstructor
public class MetricsMonitorSchedule implements InitializingBean, ApplicationListener<RefreshScopeRefreshedEvent>, DisposableBean {

    private final InstanceRegistry instanceRegistry;
    private final List<IInstanceMetricsMonitorComponent> instanceMetricsMonitorComponents;
    public static final String ENDPOINT_METRICS = "metrics";
    private List<ScheduledExecutorService> metricsMonitorSchedules = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        release();

        metricsMonitorSchedules = new ArrayList<>(instanceMetricsMonitorComponents.size());
        for (IInstanceMetricsMonitorComponent instanceMetricsMonitorService : instanceMetricsMonitorComponents) {
            ScheduledExecutorService metricsMonitorSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(instanceMetricsMonitorService.getInstanceMetric().name()));
            metricsMonitorSchedule.scheduleWithFixedDelay(() -> check(instanceMetricsMonitorService),
                    instanceMetricsMonitorService.getCheckIntervalSeconds(),
                    instanceMetricsMonitorService.getCheckIntervalSeconds(),
                    TimeUnit.SECONDS);

            metricsMonitorSchedules.add(metricsMonitorSchedule);
        }
    }

    public void check(IInstanceMetricsMonitorComponent instanceMetricsMonitorComponent) {
        List<Instance> instances = matchMetricsInstances();
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }

        Instant now = Instant.now();
        for (Instance instance : instances) {
            // 稳定阈值
            Integer preHeatHour = instanceMetricsMonitorComponent.getPreHeatHour(instance.getRegistration().getName());
            if (instance.getStatusTimestamp().plus(Duration.ofHours(preHeatHour)).isAfter(now)) {
                continue;
            }

            try {
                instanceMetricsMonitorComponent.alert(instance);
            } catch (Exception e) {
                log.error("alert.error|instance={}", instance, e);
            } finally {
                instanceMetricsMonitorComponent.truncateHistory();
            }
        }
    }

    private List<Instance> matchMetricsInstances() {
        return instanceRegistry.getInstances()
                .filter(Instance::isRegistered)
                .filter(instance -> {
                    Endpoints endpoints = instance.getEndpoints();
                    return endpoints != null && endpoints.isPresent(ENDPOINT_METRICS);
                }).collectList().block(Duration.ofMillis(10_000L));
    }

    @Override
    public void destroy() throws Exception {
        release();
    }

    private void release() {
        if (metricsMonitorSchedules == null) {
            return;
        }

        for (ScheduledExecutorService metricsMonitorSchedule : metricsMonitorSchedules) {
            try {
                metricsMonitorSchedule.shutdown();
            } catch (Exception e) {
                log.error("metricsMonitorSchedule shutdown error", metricsMonitorSchedule, e);
            }
        }
    }

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        // 处理“@RefreshScope会导致ScheduledExecutorService失效”的问题
        // do nothing
    }

}