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
 * 服务gc指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public class GcSpeedMonitorComponent extends AbstractInstanceSpeedMetricsMonitorComponent<Long> {

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
            if (matchIncreaseTooFast(name, currentGcCount)) {
                String alertDesc = String.format("gc速度超过预警值[%.2f次/分钟]", getSpeed());
                return MetricCheckResultDTO.error(MetricCheckStatus.GC_SPEED_TOO_FAST, alertDesc);
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }

        return MetricCheckResultDTO.ok();
    }

    @Override
    protected double getSpeed() {
        return monitorProperties.getMetric().getGcSpeedThreshold();
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getMetric().getGcCheckIntervalSeconds();
    }

}