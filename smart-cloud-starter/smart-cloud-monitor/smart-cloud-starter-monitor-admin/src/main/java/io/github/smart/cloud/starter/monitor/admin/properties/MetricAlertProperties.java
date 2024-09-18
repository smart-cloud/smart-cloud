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

import java.math.BigDecimal;

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
     * 堆内存指标监控配置
     */
    private MetricItemAlertProperties<DataSize> heap = new MetricItemAlertProperties<>();
    /**
     * 非堆内存指标监控配置
     */
    private MetricItemAlertProperties<DataSize> nonHeap = new MetricItemAlertProperties<>();
    /**
     * cpu指标监控配置
     */
    private MetricItemAlertProperties<Double> cpu = new MetricItemAlertProperties<>();
    /**
     * 线程指标监控配置
     */
    private MetricItemAlertProperties<Integer> thread = new MetricItemAlertProperties<>();
    /**
     * gc指标监控配置
     */
    private MetricItemAlertProperties<Double> gc = new MetricItemAlertProperties<>();
    /**
     * tomcat指标监控配置
     */
    private MetricItemAlertProperties<BigDecimal> tomcat = new MetricItemAlertProperties<>();

}