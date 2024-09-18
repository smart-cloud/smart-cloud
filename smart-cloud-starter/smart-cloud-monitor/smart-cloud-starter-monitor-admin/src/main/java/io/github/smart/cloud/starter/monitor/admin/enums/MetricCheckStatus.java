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
package io.github.smart.cloud.starter.monitor.admin.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指标检查状态
 *
 * @author collin
 * @date 2024-07-23
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MetricCheckStatus {

    /**
     * 健康
     */
    OK("健康"),
    /**
     * 超过阈值异常
     */
    THRESHOLD_EXCEPTION("超过阈值异常"),
    /**
     * 持续增长异常
     */
    KEEP_INCREASING_EXCEPTION("持续增长异常"),
    /**
     * gc太频繁异常
     */
    GC_SPEED_TOO_FAST("gc太频繁异常"),
    /**
     * 可用资源预警
     */
    AVAILABLE_RESOURCE_ALERT("可用资源预警");

    private String desc;

}