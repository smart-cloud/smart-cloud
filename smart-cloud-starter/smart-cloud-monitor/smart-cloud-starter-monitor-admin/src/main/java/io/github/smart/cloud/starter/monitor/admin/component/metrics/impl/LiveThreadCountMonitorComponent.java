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
import io.github.smart.cloud.starter.monitor.admin.util.ActuatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 服务存活线程数指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public class LiveThreadCountMonitorComponent extends AbstractInstanceMetricsMonitorComponent<Integer, Integer> {

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.LIVE_THREAD_COUNT;
    }

    @Override
    public MetricCheckResultDTO check(Instance instance) throws IOException {
        String response = ActuatorUtil.sendGetRequest(instance, getInstanceMetric().getValue());
        if (response == null || !StringUtils.hasText(response)) {
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
            JsonNode valueNode = ActuatorUtil.parseValueNode(measurementsNodes, "VALUE");
            if (valueNode == null) {
                return MetricCheckResultDTO.ok();
            }

            String name = instance.getRegistration().getName();
            int currentLiveThreadCount = valueNode.asInt();
            Integer alertThreshold = getThreshold(name);
            // 触发阈值
            if (currentLiveThreadCount >= alertThreshold) {
                String alertDesc = String.format("当前值[%d]超过预警值[%d]", currentLiveThreadCount, alertThreshold);
                return MetricCheckResultDTO.error(MetricCheckStatus.THRESHOLD_EXCEPTION, alertDesc);
            }

            // 连续新增
            MatchIncreaseResultDTO matchIncreaseResult = matchKeepIncreasing(name, instance.getId().toString(),
                    currentLiveThreadCount);
            if (matchIncreaseResult.getMatch()) {
                String alertDesc = String.format("活动线程数连续新增超过预警值[%f][%d次]，当前线程数[%f]", getDiffThreshold(name),
                        getKeepIncreasingCount(name), currentLiveThreadCount);
                return MetricCheckResultDTO.error(MetricCheckStatus.KEEP_INCREASING_EXCEPTION, alertDesc);
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }

        return MetricCheckResultDTO.ok();
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getThreadCheckIntervalSeconds();
    }

    @Override
    public Integer getPreHeatHour(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Integer> metricItemAlert = serviceInfos.get(serviceName).getMetric().getThread();
            if (metricItemAlert.getPreHeatHour() != null) {
                return metricItemAlert.getPreHeatHour();
            }
        }

        return monitorProperties.getMetric().getThread().getPreHeatHour();
    }

    @Override
    public Integer getKeepIncreasingCount(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Integer> metricItemAlert = serviceInfos.get(serviceName).getMetric().getThread();
            if (metricItemAlert.getKeepIncreasingCount() != null) {
                return metricItemAlert.getKeepIncreasingCount();
            }
        }

        return monitorProperties.getMetric().getThread().getKeepIncreasingCount();
    }

    @Override
    public Integer getKeepIncreasingSpeedThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Integer> metricItemAlert = serviceInfos.get(serviceName).getMetric().getThread();
            if (metricItemAlert.getKeepIncreasingSpeedThreshold() != null) {
                return metricItemAlert.getKeepIncreasingSpeedThreshold();
            }
        }

        return monitorProperties.getMetric().getThread().getKeepIncreasingSpeedThreshold();
    }

    @Override
    public Integer getThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<Integer> metricItemAlert = serviceInfos.get(serviceName).getMetric().getThread();
            if (metricItemAlert.getThreshold() != null) {
                return metricItemAlert.getThreshold();
            }
        }

        return monitorProperties.getMetric().getThread().getThreshold();
    }

    @Override
    protected double getDiffThreshold(String serviceName) {
        return getKeepIncreasingSpeedThreshold(serviceName) * getCheckIntervalSeconds() / 60;
    }

}