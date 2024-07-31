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

import io.github.smart.cloud.starter.monitor.admin.properties.MetricAlertProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务指标（速度）监控父类
 *
 * @param <T>
 * @author collin
 * @date 2024-07-28
 */
@Slf4j
public abstract class AbstractInstanceSpeedMetricsMonitorComponent<T extends Number> extends AbstractInstanceMetricsMonitorComponent<T> {

    /**
     * 获取速度
     *
     * @return
     */
    protected abstract double getSpeed();

    /**
     * 匹配增长太快
     *
     * @param instanceId
     * @param metricValue
     * @return true：太快；false：正常
     */
    protected boolean matchIncreaseTooFast(String instanceId, T metricValue) {
        List<T> instanceData = HISTORY_DATA.computeIfAbsent(instanceId, (key) -> new CopyOnWriteArrayList<>());
        instanceData.add(metricValue);
        int historyCount = instanceData.size();
        MetricAlertProperties metricAlertProperties = monitorProperties.getMetric();
        if (historyCount < metricAlertProperties.getKeepIncreasingTooFastCount()) {
            return false;
        }

        T lastValue = instanceData.get(historyCount - 1);
        for (int i = 1; i < metricAlertProperties.getKeepIncreasingTooFastCount(); i++) {
            T currentValue = instanceData.get(historyCount - 1 - i);
            double diff = lastValue.doubleValue() - currentValue.doubleValue();
            if (diff <= 0) {
                return false;
            } else if (diff < getSpeed() * (getCheckIntervalSeconds() / 60)) {
                return false;
            }
            lastValue = currentValue;
        }
        return true;
    }

}