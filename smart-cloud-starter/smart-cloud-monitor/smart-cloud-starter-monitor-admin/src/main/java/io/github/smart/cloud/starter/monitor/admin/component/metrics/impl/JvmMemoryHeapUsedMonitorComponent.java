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
package io.github.smart.cloud.starter.monitor.admin.component.metrics.impl;

import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import io.github.smart.cloud.starter.monitor.admin.properties.MetricItemAlertProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import org.springframework.util.unit.DataSize;

import java.util.Map;

/**
 * 服务堆内内存指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
public class JvmMemoryHeapUsedMonitorComponent extends AbstractJvmMemoryUsedMonitorComponent {

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.HEAP;
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getMemoryCheckIntervalSeconds();
    }

    @Override
    public Integer getPreHeatHour(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<DataSize> metricItemAlert = serviceInfos.get(serviceName).getMetric().getHeap();
            if (metricItemAlert.getPreHeatHour() != null) {
                return metricItemAlert.getPreHeatHour();
            }
        }

        return monitorProperties.getMetric().getHeap().getPreHeatHour();
    }

    @Override
    public Integer getKeepIncreasingCount(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<DataSize> metricItemAlert = serviceInfos.get(serviceName).getMetric().getHeap();
            if (metricItemAlert.getKeepIncreasingCount() != null) {
                return metricItemAlert.getKeepIncreasingCount();
            }
        }

        return monitorProperties.getMetric().getHeap().getKeepIncreasingCount();
    }

    @Override
    public DataSize getKeepIncreasingSpeedThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<DataSize> metricItemAlert = serviceInfos.get(serviceName).getMetric().getHeap();
            if (metricItemAlert.getKeepIncreasingSpeedThreshold() != null) {
                return metricItemAlert.getKeepIncreasingSpeedThreshold();
            }
        }

        return monitorProperties.getMetric().getHeap().getKeepIncreasingSpeedThreshold();
    }

    @Override
    public DataSize getThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<DataSize> metricItemAlert = serviceInfos.get(serviceName).getMetric().getHeap();
            if (metricItemAlert.getThreshold() != null) {
                return metricItemAlert.getThreshold();
            }
        }

        return monitorProperties.getMetric().getHeap().getThreshold();
    }

}