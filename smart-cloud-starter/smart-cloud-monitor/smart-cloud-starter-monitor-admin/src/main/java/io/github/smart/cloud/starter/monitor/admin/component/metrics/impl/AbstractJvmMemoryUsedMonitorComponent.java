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
import io.github.smart.cloud.starter.monitor.admin.enums.MetricCheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;

import java.io.IOException;

/**
 * 服务使用内存指标监控父类
 *
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public abstract class AbstractJvmMemoryUsedMonitorComponent extends AbstractInstanceMetricsMonitorComponent<Long, DataSize> {

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

            DataSize currentSize = DataSize.ofBytes(valueNode.asLong());
            String serviceName = instance.getRegistration().getName();
            DataSize threshold = getThreshold(serviceName);
            // 触发阈值
            if (currentSize.compareTo(threshold) >= 0) {
                String alertDesc = String.format("当前值[%dMB]超过预警值[%dMB]", currentSize.toMegabytes(), threshold.toMegabytes());
                return MetricCheckResultDTO.error(MetricCheckStatus.THRESHOLD_EXCEPTION, alertDesc);
            }

            // 连续新增
            MatchIncreaseResultDTO matchIncreaseResult = matchKeepIncreasing(serviceName, instance.getId().toString(), valueNode.asLong());
            if (matchIncreaseResult.isMatch()) {
                String alertDesc = String.format("内存连续新增超过预警值[%dMB][%d次]，当前内值[%dMB]，有内存泄漏倾向",
                        getKeepIncreasingCount(serviceName), currentSize.toMegabytes());
                return MetricCheckResultDTO.error(MetricCheckStatus.KEEP_INCREASING_EXCEPTION, alertDesc);
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }

        return MetricCheckResultDTO.ok();
    }

    @Override
    protected double getDiffThreshold(String serviceName) {
        return getKeepIncreasingSpeedThreshold(serviceName).toBytes() * getCheckIntervalSeconds() / 60;
    }

}