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

import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;

import java.io.IOException;

/**
 * 服务实例指标监控
 *
 * @author collin
 * @date 2024-07-28
 */
public interface IInstanceMetricsMonitorComponent {

    /**
     * 获取实例监控维度
     *
     * @return
     */
    InstanceMetric getInstanceMetric();

    /**
     * 检查指标
     *
     * @param instance
     * @return
     * @throws IOException
     */
    MetricCheckResultDTO check(Instance instance) throws IOException;

    /**
     * 预警通知
     *
     * @param instance
     * @return true：已触发预警；false：没有触发预警
     * @throws IOException
     */
    boolean alert(Instance instance) throws IOException;

    /**
     * 清理历史记录
     */
    void truncateHistory();

    /**
     * 获取检查间隔时间
     *
     * @return
     */
    long getCheckIntervalSeconds();

}