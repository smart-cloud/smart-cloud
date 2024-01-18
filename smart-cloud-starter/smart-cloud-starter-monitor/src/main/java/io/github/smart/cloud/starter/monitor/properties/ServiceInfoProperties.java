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

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 服务配置
 *
 * @author collin
 * @date 2023-01-16
 */
@Getter
@Setter
@ToString
public class ServiceInfoProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * gitlab工程ID
     */
    private Long id;
    /**
     * 企业微信提醒人（账号）
     */
    private Set<String> reminders = new LinkedHashSet<>();
    /**
     * 提醒的tag最小时间间隔
     */
    private long remindTagMinDiffTs = 3600 * 1000L;
    /**
     * 机器人key
     */
    private String robotKey;

}