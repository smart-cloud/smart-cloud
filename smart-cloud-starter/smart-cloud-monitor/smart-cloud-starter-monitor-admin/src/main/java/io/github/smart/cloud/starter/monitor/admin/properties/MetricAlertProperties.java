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
package io.github.smart.cloud.starter.monitor.admin.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

/**
 * 服务指标监控配置
 *
 * @author collin
 * @date 2024-07-28
 */
@Getter
@Setter
@ToString
public class MetricAlertProperties {

    /**
     * 默认预热期（单位：小时）
     */
    private Integer preHeatHour = 4;
    /**
     * 默认堆内存预警值
     *
     * @see DataSize#parse
     */
    private DataSize heapThreshold = DataSize.of(3, DataUnit.GIGABYTES);
    /**
     * 默认非堆内存预警值
     *
     * @see DataSize#parse
     */
    private DataSize nonHeapThreshold = DataSize.of(2, DataUnit.GIGABYTES);
    /**
     * 默认连续增长的次数阈值
     */
    private Integer keepIncreasingCount = 5;
    /**
     * cpu使用率预警值
     */
    private double cpuUsageThreshold = 0.95d;
    /**
     * 活动线程数预警值
     */
    private int liveThreadCountThreshold = 1000;

    /**
     * gc速度预警值（单位：次数/分钟）
     */
    private double gcSpeedThreshold = 6.0d;
    /**
     * 默认连续增长太快的次数阈值
     */
    private Integer keepIncreasingTooFastCount = 3;

    /**
     * gc检查间隔时间（单位：秒）
     */
    private long gcCheckIntervalSeconds = 10 * 60L;
    /**
     * cpu检查间隔时间（单位：秒）
     */
    private long cpuCheckIntervalSeconds = 10 * 60L;
    /**
     * 堆（外）内存检查间隔时间（单位：秒）
     */
    private long memoryCheckIntervalSeconds = 120 * 60L;
    /**
     * 存活线程检查间隔时间（单位：秒）
     */
    private long threadCheckIntervalSeconds = 30 * 60L;

}