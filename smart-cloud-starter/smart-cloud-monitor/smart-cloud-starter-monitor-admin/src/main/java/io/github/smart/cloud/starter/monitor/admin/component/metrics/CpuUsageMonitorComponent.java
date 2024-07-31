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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import io.github.smart.cloud.starter.monitor.admin.enums.MetricCheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 服务cpu使用率指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public class CpuUsageMonitorComponent extends AbstractInstanceMetricsMonitorComponent<Double> {

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.CPU;
    }

    @Override
    public MetricCheckResultDTO check(Instance instance) throws IOException {
        String response = sendGetRequest(instance);
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

            JsonNode valueNode = getValueNode(measurementsNodes, "VALUE");
            if (valueNode == null) {
                return MetricCheckResultDTO.ok();
            }

            String name = instance.getRegistration().getName();
            double currentCpuUsage = valueNode.asDouble();
            double alertThreshold = monitorProperties.getMetric().getCpuUsageThreshold();
            // 触发阈值
            if (currentCpuUsage >= alertThreshold) {
                String alertDesc = String.format("当前值[%.4f]超过预警值[%.4f]", currentCpuUsage, alertThreshold);
                return MetricCheckResultDTO.error(MetricCheckStatus.THRESHOLD_EXCEPTION, alertDesc);
            }

            // 连续新增
            if (matchKeepIncreasing(name, currentCpuUsage)) {
                String alertDesc = String.format("cpu使用率连续新增超过预警值[%d次]，当前使用率[%.4f]", monitorProperties.getMetric().getKeepIncreasingCount(), currentCpuUsage);
                return MetricCheckResultDTO.error(MetricCheckStatus.KEEP_INCREASING_EXCEPTION, alertDesc);
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }

        return MetricCheckResultDTO.ok();
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getMetric().getCpuCheckIntervalSeconds();
    }

}