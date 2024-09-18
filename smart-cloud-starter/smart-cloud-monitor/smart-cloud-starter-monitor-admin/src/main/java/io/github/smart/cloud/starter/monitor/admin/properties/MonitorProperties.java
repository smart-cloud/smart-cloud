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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 监控配置信息
 *
 * @author collin
 * @date 2023-01-16
 */
@Getter
@Setter
@ToString
public class MonitorProperties implements InitializingBean {

    /**
     * 服务启动时过滤消息时间间隔（单位：毫秒）
     */
    private Long filterEventTs = 60 * 1000L;
    /**
     * 检查离线服务在线实例数间隔时间（单位：毫秒）
     */
    private Long checkOfflineTs = 60 * 5 * 1000L;
    /**
     * 检查服务在线实例数间隔时间（单位：毫秒）
     */
    private Long checkServiceNodeCountTs = 60 * 20 * 1000L;
    /**
     * 工程信息
     */
    private ProxyProperties proxy = new ProxyProperties();
    /**
     * gitlab配置
     */
    private GitlabProperties gitlab = new GitlabProperties();
    /**
     * 默认的机器人key
     */
    private String robotKey;
    /**
     * 服务配置
     */
    private Map<String, ServiceInfoProperties> serviceInfos = new HashMap<>();
    /**
     * 不监听的服务
     */
    private Set<String> excludeServices = new HashSet<>();
    /**
     * 不监听的一直离线服务
     */
    private Set<String> excludeOfflineCheckServices = new HashSet<>();

    /**
     * 服务内存指标检查间隔时间（单位：秒）
     */
    private Long memoryCheckIntervalSeconds = 30 * 60L;
    /**
     * 服务cpu指标检查间隔时间（单位：秒）
     */
    private Long cpuCheckIntervalSeconds = 10 * 60L;
    /**
     * 服务线程指标检查间隔时间（单位：秒）
     */
    private Long threadCheckIntervalSeconds = 20 * 60L;
    /**
     * 服务gc指标检查间隔时间（单位：秒）
     */
    private Long gcCheckIntervalSeconds = 10 * 60L;
    /**
     * 服务tomcat指标检查间隔时间（单位：秒）
     */
    private Long tomcatCheckIntervalSeconds = 5 * 60L;
    /**
     * 默认指标监控阈值
     */
    private MetricAlertProperties metric = new MetricAlertProperties();

    @Override
    public void afterPropertiesSet() throws Exception {
        settingDefaultHeap();
        settingDefaultNonHeap();
        settingDefaultCpu();
        settingDefaultThread();
        settingDefaultGc();
        settingDefaultTomcat();
    }

    private void settingDefaultHeap() {
        MetricItemAlertProperties<DataSize> heap = metric.getHeap();
        if (heap.getPreHeatHour() == null) {
            heap.setPreHeatHour(4);
        }
        if (heap.getKeepIncreasingCount() == null) {
            heap.setKeepIncreasingCount(5);
        }
        if (heap.getKeepIncreasingSpeedThreshold() == null) {
            heap.setKeepIncreasingSpeedThreshold(DataSize.of(10, DataUnit.MEGABYTES));
        }
        if (heap.getThreshold() == null) {
            heap.setThreshold(DataSize.of(3, DataUnit.GIGABYTES));
        }
    }

    private void settingDefaultNonHeap() {
        MetricItemAlertProperties<DataSize> nonHeap = metric.getNonHeap();
        if (nonHeap.getPreHeatHour() == null) {
            nonHeap.setPreHeatHour(4);
        }
        if (nonHeap.getKeepIncreasingCount() == null) {
            nonHeap.setKeepIncreasingCount(5);
        }
        if (nonHeap.getKeepIncreasingSpeedThreshold() == null) {
            nonHeap.setKeepIncreasingSpeedThreshold(DataSize.of(5, DataUnit.MEGABYTES));
        }
        if (nonHeap.getThreshold() == null) {
            nonHeap.setThreshold(DataSize.of(2, DataUnit.GIGABYTES));
        }
    }

    private void settingDefaultCpu() {
        MetricItemAlertProperties<Double> cpu = metric.getCpu();
        if (cpu.getPreHeatHour() == null) {
            cpu.setPreHeatHour(2);
        }
        if (cpu.getKeepIncreasingCount() == null) {
            cpu.setKeepIncreasingCount(5);
        }
        if (cpu.getKeepIncreasingSpeedThreshold() == null) {
            cpu.setKeepIncreasingSpeedThreshold(0.50d);
        }
        if (cpu.getThreshold() == null) {
            cpu.setThreshold(0.90d);
        }
    }

    private void settingDefaultThread() {
        MetricItemAlertProperties<Integer> thread = metric.getThread();
        if (thread.getPreHeatHour() == null) {
            thread.setPreHeatHour(2);
        }
        if (thread.getKeepIncreasingCount() == null) {
            thread.setKeepIncreasingCount(3);
        }
        if (thread.getKeepIncreasingSpeedThreshold() == null) {
            thread.setKeepIncreasingSpeedThreshold(1);
        }
        if (thread.getThreshold() == null) {
            thread.setThreshold(1000);
        }
    }

    private void settingDefaultGc() {
        MetricItemAlertProperties<Double> gc = metric.getGc();
        if (gc.getPreHeatHour() == null) {
            gc.setPreHeatHour(2);
        }
        if (gc.getKeepIncreasingCount() == null) {
            gc.setKeepIncreasingCount(3);
        }
        if (gc.getKeepIncreasingSpeedThreshold() == null) {
            gc.setKeepIncreasingSpeedThreshold(6.0);
        }
    }

    private void settingDefaultTomcat() {
        MetricItemAlertProperties<BigDecimal> gc = metric.getTomcat();
        if (gc.getPreHeatHour() == null) {
            gc.setPreHeatHour(1);
        }
        if (gc.getThreshold() == null) {
            gc.setThreshold(BigDecimal.valueOf(0.85D));
        }
    }

}