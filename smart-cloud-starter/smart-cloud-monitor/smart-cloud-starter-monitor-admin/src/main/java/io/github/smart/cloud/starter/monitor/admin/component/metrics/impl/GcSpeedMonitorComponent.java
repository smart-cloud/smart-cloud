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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import io.github.smart.cloud.starter.monitor.admin.enums.MetricCheckStatus;
import io.github.smart.cloud.starter.monitor.admin.properties.MetricItemAlertProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务gc指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public class GcSpeedMonitorComponent extends AbstractInstanceMetricsMonitorComponent<Long, Double> {

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.GC_INFO;
    }

    @Override
    public MetricCheckResultDTO check(Instance instance) throws IOException {
        String response = sendGetRequest(instance);
        if (!StringUtils.hasText(response)) {
            return MetricCheckResultDTO.ok();
        }

        try {
            JsonNode responseNode = objectMapper.readTree(response);
            if (responseNode == null) {
                return MetricCheckResultDTO.ok();
            }
            JsonNode measurementsNodes = responseNode.get("measurements");
            if (measurementsNodes == null || measurementsNodes.isEmpty()) {
                return MetricCheckResultDTO.ok();
            }

            JsonNode valueNode = getValueNode(measurementsNodes, "COUNT");
            if (valueNode == null) {
                return MetricCheckResultDTO.ok();
            }

            String name = instance.getRegistration().getName();
            long currentGcCount = valueNode.asLong();
            // gc速度太快
            if (matchIncreaseTooFast(name, instance.getId().toString(), currentGcCount)) {
                String alertDesc = String.format("gc速度超过预警值[%.2f次/分钟]", getKeepIncreasingSpeedThreshold(name));
                return MetricCheckResultDTO.error(MetricCheckStatus.GC_SPEED_TOO_FAST, alertDesc);
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }

        return MetricCheckResultDTO.ok();
    }

    /**
     * 匹配增长太快
     *
     * @param instanceId
     * @param metricValue
     * @return true：太快；false：正常
     */
    private boolean matchIncreaseTooFast(String serviceName, String instanceId, Long metricValue) {
        List<Long> instanceData = HISTORY_DATA.computeIfAbsent(instanceId, (key) -> new CopyOnWriteArrayList<>());
        instanceData.add(metricValue);
        int historyCount = instanceData.size();
        Integer keepIncreasingCount = getKeepIncreasingCount(serviceName);
        if (historyCount < keepIncreasingCount) {
            return false;
        }

        Long lastValue = instanceData.get(historyCount - 1);
        Long checkIntervalSeconds = getCheckIntervalSeconds();
        for (int i = 1; i < keepIncreasingCount; i++) {
            Long currentValue = instanceData.get(historyCount - 1 - i);
            double diff = lastValue.doubleValue() - currentValue.doubleValue();
            if (diff <= 0) {
                return false;
            } else if (diff < getKeepIncreasingSpeedThreshold(serviceName) * (checkIntervalSeconds / 60)) {
                return false;
            }
            lastValue = currentValue;
        }
        return true;
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getGcCheckIntervalSeconds();
    }

    @Override
    public Integer getPreHeatHour(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getGc();
            if (metricItemAlert.getPreHeatHour() != null) {
                return metricItemAlert.getPreHeatHour();
            }
        }

        return monitorProperties.getMetric().getGc().getPreHeatHour();
    }

    @Override
    public Integer getKeepIncreasingCount(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getGc();
            if (metricItemAlert.getKeepIncreasingCount() != null) {
                return metricItemAlert.getKeepIncreasingCount();
            }
        }

        return monitorProperties.getMetric().getGc().getKeepIncreasingCount();
    }

    @Override
    public Double getKeepIncreasingSpeedThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getGc();
            if (metricItemAlert.getKeepIncreasingSpeedThreshold() != null) {
                return metricItemAlert.getKeepIncreasingSpeedThreshold();
            }
        }

        return monitorProperties.getMetric().getGc().getKeepIncreasingSpeedThreshold();
    }

    @Override
    public Double getThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getGc();
            if (metricItemAlert.getThreshold() != null) {
                return metricItemAlert.getThreshold();
            }
        }

        return monitorProperties.getMetric().getGc().getThreshold();
    }

}