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
package io.github.smart.cloud.starter.monitor.admin.component.metrics;

import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import org.springframework.util.unit.DataSize;

import java.util.Map;

/**
 * 服务堆外内存指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
public class JvmMemoryNonHeapUsedMonitorComponent extends AbstractJvmMemoryUsedMonitorComponent {

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.NON_HEAP;
    }

    @Override
    protected DataSize getAlertThreshold(MonitorProperties monitorProperties, String instanceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos == null || serviceInfos.isEmpty()) {
            return monitorProperties.getMetric().getNonHeapThreshold();
        }

        ServiceInfoProperties serviceInfo = serviceInfos.get(instanceName);
        if (serviceInfo == null || serviceInfo.getNonHeapAlertThreshold() == null) {
            return monitorProperties.getMetric().getNonHeapThreshold();
        }

        return serviceInfo.getNonHeapAlertThreshold();
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getMetric().getMemoryCheckIntervalSeconds();
    }

}