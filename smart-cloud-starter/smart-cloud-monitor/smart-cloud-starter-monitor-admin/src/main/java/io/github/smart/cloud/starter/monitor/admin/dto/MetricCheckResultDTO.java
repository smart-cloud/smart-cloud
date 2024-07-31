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
package io.github.smart.cloud.starter.monitor.admin.dto;

import io.github.smart.cloud.starter.monitor.admin.enums.MetricCheckStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 维度检查结果
 *
 * @author collin
 * @date 2024-07-28
 */
@Getter
@Setter
@ToString
public class MetricCheckResultDTO {

    /**
     * 检查结果
     */
    private MetricCheckStatus metricCheckStatus;
    /**
     * 预警描述
     */
    private String alertDesc;

    public static MetricCheckResultDTO ok() {
        MetricCheckResultDTO ok = new MetricCheckResultDTO();
        ok.setMetricCheckStatus(MetricCheckStatus.OK);
        return ok;
    }

    public static MetricCheckResultDTO error(MetricCheckStatus metricCheckStatus, String alertDesc) {
        MetricCheckResultDTO metricCheckResult = new MetricCheckResultDTO();
        metricCheckResult.setMetricCheckStatus(metricCheckStatus);
        metricCheckResult.setAlertDesc(alertDesc);
        return metricCheckResult;
    }

}