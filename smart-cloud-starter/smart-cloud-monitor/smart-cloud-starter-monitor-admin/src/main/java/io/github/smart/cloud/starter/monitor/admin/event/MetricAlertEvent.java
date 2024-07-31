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
package io.github.smart.cloud.starter.monitor.admin.event;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 服务指标预警事件
 *
 * @author collin
 * @date 2024-07-28
 */
@Getter
@Setter
@ToString
public class MetricAlertEvent extends ApplicationEvent {

    /**
     * 实例信息
     */
    private Instance instance;
    /**
     * 指标
     */
    private InstanceMetric instanceMetric;
    /**
     * 检查结果
     */
    private MetricCheckResultDTO metricCheckResult;

    public MetricAlertEvent(Object source) {
        super(source);
    }

}