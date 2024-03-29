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
package io.github.smart.cloud.starter.monitor.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class MonitorProperties {

    /**
     * 服务启动时过滤消息时间间隔（单位：毫秒）
     */
    private Long filterEventTs = 60 * 1000L;
    /**
     * 检查离线服务在线实例数间隔时间（单位：毫秒）
     */
    private Long checkOfflineTs = 60 * 5 * 1000L;
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

}