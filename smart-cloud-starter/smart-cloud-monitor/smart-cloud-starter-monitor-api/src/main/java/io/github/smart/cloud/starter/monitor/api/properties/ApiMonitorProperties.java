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
package io.github.smart.cloud.starter.monitor.api.properties;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

/**
 * 接口健康检测配置属性
 * <p/>
 * <b>配置样例：</b>
 * <pre>
 * api-monitor:
 *   unhealthMatchMinCount: 10
 *   defaultFailRateThreshold: 0.3
 *   failRateThresholds:
 *     '[LoginController#login]': 0
 *     '[OrderController#query]': 0
 * </pre>
 *
 * @author collin
 * @date 2024-01-6
 */
@Getter
@Setter
public class ApiMonitorProperties {

    public static final String PREFIX = "smart.api-monitor";

    /**
     * 不健康匹配最小数量
     */
    private int unhealthMatchMinCount = 5;
    /**
     * 不健康接口最大上报数
     */
    private int unhealthApiReportMaxCount = 10;
    /**
     * 默认失败阈值（默认0.5）
     */
    private BigDecimal defaultFailRateThreshold = BigDecimal.valueOf(0.5);
    /**
     * 特定接口失败阈值
     */
    private Map<String, BigDecimal> failRateThresholds = new LinkedHashMap<>();
    /**
     * 接口白名单（不监听异常）
     */
    private Set<String> apiWhiteList = new HashSet<>();

    // -------企业微信通知配置 start
    /**
     * 清理间隔时间（单位：秒）
     */
    private long cleanIntervalSeconds = 60 * 3L;
    /**
     * 异常接口通知间隔时间（单位：秒）
     */
    private long apiExceptionNoticeIntervalSeconds = 60L;
    /**
     * 发送消息时的代理host
     */
    private String proxyHost;
    /**
     * 发送消息时的代理端口
     */
    private int port;
    /**
     * 企业微信机器人key
     */
    private String robotKey;
    /**
     * 异常提醒人
     */
    private Set<String> mentionedList = new LinkedHashSet<>();
    /**
     * “需要提醒的异常类名列表”中命中时，需要提醒
     */
    private Boolean alertExceptionMarked = true;
    /**
     * 需要提醒的异常类名列表
     */
    private Set<String> needAlertExceptionClassNames = new HashSet<>();
    /**
     * 需要提醒的异常码列表
     */
    private Set<String> needAlertExceptionCodes = new HashSet<>();
    // -------企业微信通知配置 end

}