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
import io.github.smart.cloud.starter.monitor.admin.dto.MatchIncreaseResultDTO;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import io.github.smart.cloud.starter.monitor.admin.enums.MetricCheckStatus;
import io.github.smart.cloud.starter.monitor.admin.properties.MetricItemAlertProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 服务cpu使用率指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public class CpuUsageMonitorComponent extends AbstractInstanceMetricsMonitorComponent<Double, Double> {

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.CPU;
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

            JsonNode valueNode = getValueNode(measurementsNodes, "VALUE");
            if (valueNode == null) {
                return MetricCheckResultDTO.ok();
            }

            String name = instance.getRegistration().getName();
            double currentCpuUsage = valueNode.asDouble();
            double alertThreshold = getThreshold(name);
            // 触发阈值
            if (currentCpuUsage >= alertThreshold) {
                String alertDesc = String.format("当前值[%.4f]超过预警值[%.4f]", currentCpuUsage, alertThreshold);
                return MetricCheckResultDTO.error(MetricCheckStatus.THRESHOLD_EXCEPTION, alertDesc);
            }

            // 连续新增
            MatchIncreaseResultDTO matchIncreaseResult = matchKeepIncreasing(name, instance.getId().toString(), currentCpuUsage);
            if (matchIncreaseResult.isMatch()) {
                String alertDesc = String.format("cpu使用率连续新增超过预警值[%.4f][%d次]，当前使用率[%.4f]", getDiffThreshold(name),
                        getKeepIncreasingCount(name), currentCpuUsage);
                return MetricCheckResultDTO.error(MetricCheckStatus.KEEP_INCREASING_EXCEPTION, alertDesc);
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }

        return MetricCheckResultDTO.ok();
    }

    @Override
    protected double getDiffThreshold(String serviceName) {
        return getKeepIncreasingSpeedThreshold(serviceName) * getCheckIntervalSeconds() / 60.0D;
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getCpuCheckIntervalSeconds();
    }

    @Override
    public Integer getPreHeatHour(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getCpu();
            if (metricItemAlert.getPreHeatHour() != null) {
                return metricItemAlert.getPreHeatHour();
            }
        }

        return monitorProperties.getMetric().getCpu().getPreHeatHour();
    }

    @Override
    public Integer getKeepIncreasingCount(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getCpu();
            if (metricItemAlert.getKeepIncreasingCount() != null) {
                return metricItemAlert.getKeepIncreasingCount();
            }
        }

        return monitorProperties.getMetric().getCpu().getKeepIncreasingCount();
    }

    @Override
    public Double getKeepIncreasingSpeedThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Double> metricItemAlert = serviceInfos.get(serviceName).getMetric().getCpu();
            if (metricItemAlert.getKeepIncreasingSpeedThreshold() != null) {
                return metricItemAlert.getKeepIncreasingSpeedThreshold();
            }
        }

        return monitorProperties.getMetric().getCpu().getKeepIncreasingSpeedThreshold();
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

        return monitorProperties.getMetric().getCpu().getThreshold();
    }

}