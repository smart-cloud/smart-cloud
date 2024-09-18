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

/**
 * 指标属性接口
 *
 * @param <U>
 * @author collin
 * @date 2024-08-03
 */
public interface IMetricProperties<U> {

    /**
     * 获取预热期
     *
     * @param serviceName
     * @return
     */
    Integer getPreHeatHour(String serviceName);

    /**
     * 获取连续增长的次数阈值
     *
     * @param serviceName
     * @return
     */
    Integer getKeepIncreasingCount(String serviceName);

    /**
     * 获取连续增长的速度阈值
     *
     * @param serviceName
     * @return
     */
    U getKeepIncreasingSpeedThreshold(String serviceName);

    /**
     * 获取预警值
     *
     * @param serviceName
     * @return
     */
    U getThreshold(String serviceName);

    /**
     * 获取检查间隔时间
     *
     * @return
     */
    long getCheckIntervalSeconds();

}