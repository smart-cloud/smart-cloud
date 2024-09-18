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
 * 服务监控指标
 * <p>指标值可通过actuator接口获取（http://localhost:8080/actuator/metric）</p>
 *
 * @author collin
 * @date 2024-07-28
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum InstanceMetric {

    /**
     * cpu
     */
    CPU("cpu", "process.cpu.usage", "cpu使用率"),
    /**
     * gc信息
     */
    GC_INFO("gc", "jvm.gc.pause", "gc信息"),
    /**
     * 活动线程数
     */
    LIVE_THREAD_COUNT("thread", "jvm.threads.live", "活动线程数"),
    /**
     * 堆内存
     */
    HEAP("heap", "jvm.memory.used?tag=area:heap", "堆内存"),
    /**
     * 堆外内存
     */
    NON_HEAP("non_heap", "jvm.memory.used?tag=area:nonheap", "堆外内存"),
    /**
     * tomcat性能指标
     */
    TOMCAT("tomcat", null, "tomcat性能指标");

    /**
     * 指标名称
     */
    private String name;
    /**
     * metrics接口请求参数
     */
    private String value;
    /**
     * 监控预警描述
     */
    private String desc;

}