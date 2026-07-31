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
package io.github.smart.cloud.starter.monitor.api.core.message;

import io.github.smart.cloud.monitor.common.dto.wework.AbstractWeworkRobotMessageDTO;
import io.github.smart.cloud.monitor.common.dto.wework.WeworkRobotMarkdownMessageDTO;
import io.github.smart.cloud.monitor.common.dto.wework.WeworkRobotTextMessageDTO;
import io.github.smart.cloud.monitor.common.enums.WeworkRobotMessageType;
import io.github.smart.cloud.monitor.common.util.MarkdownUtil;
import io.github.smart.cloud.starter.monitor.api.core.IMessageFactory;
import io.github.smart.cloud.starter.monitor.api.dto.ApiExceptionAlertDTO;
import io.github.smart.cloud.starter.monitor.api.enums.ApiExceptionRemindType;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.starter.monitor.api.properties.ExceptionApiMonitorProperties;
import io.github.smart.cloud.utility.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 接口异常消息工厂
 *
 * @author collin.li
 * @date 2025-10-22
 */
public class ApiExceptionMessageFactory extends AbstractMessageFactory implements IMessageFactory<ApiExceptionAlertDTO> {

    public ApiExceptionMessageFactory(ApiMonitorProperties apiMonitorProperties) {
        super(apiMonitorProperties);
    }

    /**
     * 构建企业微信机器人消息体
     *
     * @param apiExceptions
     * @return
     */
    @Override
    public AbstractWeworkRobotMessageDTO buildSummaryAlertMessages(List<ApiExceptionAlertDTO> apiExceptions) {
        if (WeworkRobotMessageType.MARKDOWN == apiMonitorProperties.getExceptionApiMonitor().getMessageType()) {
            return buildSummaryAlertMarkdownMessages(apiExceptions);
        } else {
            return buildSummaryAlertTextMessage(apiExceptions);
        }
    }

    /**
     * 构建企业微信机器人消息体（立即通知）
     *
     * @param apiExceptionAlert
     * @return
     */
    @Override
    public AbstractWeworkRobotMessageDTO buildImmediateAlertMessage(ApiExceptionAlertDTO apiExceptionAlert) {
        if (WeworkRobotMessageType.MARKDOWN == apiMonitorProperties.getExceptionApiMonitor().getMessageType()) {
            StringBuilder content = new StringBuilder(128);
            content.append("**").append(appName).append("**")
                    .append("接口异常:")
                    .append("\n**IP**：").append(ip)
                    .append("\n**时间**：").append(DateUtil.getCurrentDateTime())
                    .append("\n**接口**：").append(apiExceptionAlert.getName());
            if (apiExceptionAlert.getTraceId() != null) {
                content.append("\n**traceId**：").append(apiExceptionAlert.getTraceId());
            }
            if (apiExceptionAlert.getSpanId() != null) {
                content.append("\n**spanId**：").append(apiExceptionAlert.getSpanId());
            }
            if (apiExceptionAlert.getRemindType() == ApiExceptionRemindType.CONSECUTIVE_FAILURE) {
                content.append("\n**连续失败**：").append("<font color=\"warning\">").append(apiExceptionAlert.getConsecutiveFailCount()).append("次</font>");
            }
            content.append("\n**异常信息**：")
                    .append("<font color=\"warning\">")
                    .append(MarkdownUtil.escape(apiExceptionAlert.getThrowable().toString()))
                    .append("</font>");

            if (!CollectionUtils.isEmpty(apiMonitorProperties.getExceptionApiMonitor().getReminders())) {
                content.append("\n\n<@").append(StringUtils.join(apiMonitorProperties.getExceptionApiMonitor().getReminders(), ">\n<@")).append(">");
            }
            return new WeworkRobotMarkdownMessageDTO(content.toString());
        } else {
            StringBuilder content = new StringBuilder(128);
            content.append("【").append(appName).append("】")
                    .append("异常接口:")
                    .append("\n【IP】：").append(ip)
                    .append("\n【时间】：").append(DateUtil.getCurrentDateTime())
                    .append("\n【接口】：").append(apiExceptionAlert.getName());
            if (apiExceptionAlert.getTraceId() != null) {
                content.append("\n【traceId】：").append(apiExceptionAlert.getTraceId());
            }
            if (apiExceptionAlert.getSpanId() != null) {
                content.append("\n【spanId】：").append(apiExceptionAlert.getSpanId());
            }
            if (apiExceptionAlert.getRemindType() == ApiExceptionRemindType.CONSECUTIVE_FAILURE) {
                content.append("\n⚠【连续失败】：").append(apiExceptionAlert.getConsecutiveFailCount()).append("次");
            }
            content.append("\n⚠【异常信息】：").append(apiExceptionAlert.getThrowable().toString());
            return new WeworkRobotTextMessageDTO(content.toString(), apiMonitorProperties.getExceptionApiMonitor().getReminders());
        }
    }

    /**
     * 构造企业微信机器人markdown格式消息
     *
     * @param apiExceptions
     * @return
     */
    private AbstractWeworkRobotMessageDTO buildSummaryAlertMarkdownMessages(List<ApiExceptionAlertDTO> apiExceptions) {
        ExceptionApiMonitorProperties exceptionApiMonitor = apiMonitorProperties.getExceptionApiMonitor();
        StringBuilder content = new StringBuilder(128);
        content.append("**").append(appName).append("**")
                .append(TimeUnit.SECONDS.toMinutes(apiMonitorProperties.getCleanIntervalSeconds()))
                .append("分钟异常接口统计:")
                .append("\n**IP**：").append(ip);
        boolean needMention = false;

        for (int i = 0; i < apiExceptions.size(); i++) {
            ApiExceptionAlertDTO apiException = apiExceptions.get(i);
            boolean isFailRateRemindType = apiException.getRemindType() == ApiExceptionRemindType.FAIL_RATE;

            content.append("\n\n>**接口").append(i + 1).append("**：").append(apiException.getName())
                    .append("\n>**请求总数**：").append(apiException.getTotalCount())
                    .append("\n>**失败数**：").append(apiException.getFailCount())
                    .append("\n>**失败率**：")
                    .append(isFailRateRemindType ? "<font color=\"warning\">" : StringUtils.EMPTY)
                    .append(apiException.getFailRate())
                    .append(isFailRateRemindType ? "</font>" : StringUtils.EMPTY);
            if (apiException.getTraceId() != null) {
                content.append("\n>**traceId**：").append(apiException.getTraceId());
            }
            if (apiException.getSpanId() != null) {
                content.append("\n>**spanId**：").append(apiException.getSpanId());
            }
            if (apiException.getThrowable() != null) {
                boolean isExceptionRemindType = apiException.getRemindType() == ApiExceptionRemindType.EXCEPTION_INFO;
                content.append("\n>**异常信息**：")
                        .append(isExceptionRemindType ? "<font color=\"warning\">" : StringUtils.EMPTY)
                        .append(MarkdownUtil.escape(apiException.getThrowable().toString()))
                        .append(isExceptionRemindType ? "</font>" : StringUtils.EMPTY);
            }
            needMention |= apiException.getNeedAtSomeone();
        }

        if (needMention && !CollectionUtils.isEmpty(exceptionApiMonitor.getReminders())) {
            content.append("\n\n<@").append(StringUtils.join(exceptionApiMonitor.getReminders(), ">\n<@")).append(">");
        }

        return new WeworkRobotMarkdownMessageDTO(content.toString());
    }

    /**
     * 构造企业微信机器人text格式消息
     *
     * @param apiExceptions
     * @return
     */
    private AbstractWeworkRobotMessageDTO buildSummaryAlertTextMessage(List<ApiExceptionAlertDTO> apiExceptions) {
        StringBuilder content = new StringBuilder(128);
        content.append("【").append(appName).append("】")
                .append(TimeUnit.SECONDS.toMinutes(apiMonitorProperties.getCleanIntervalSeconds()))
                .append("分钟异常接口统计:")
                .append("\n【IP】：").append(ip);

        boolean needMention = false;
        int exceptionCount = apiExceptions.size();
        for (int i = 0; i < exceptionCount; i++) {
            ApiExceptionAlertDTO apiException = apiExceptions.get(i);
            boolean isFailRateRemindType = apiException.getRemindType() == ApiExceptionRemindType.FAIL_RATE;
            if (exceptionCount > 1) {
                content.append("\n───────────────────────");
            }
            content.append("\n【接口").append(i + 1).append("】：").append(apiException.getName())
                    .append("\n【请求总数】：").append(apiException.getTotalCount())
                    .append("\n【失败数】：").append(apiException.getFailCount())
                    .append("\n").append(isFailRateRemindType ? "⚠" : StringUtils.EMPTY).append("【失败率】：").append(apiException.getFailRate());
            if (apiException.getTraceId() != null) {
                content.append("\n【traceId】：").append(apiException.getTraceId());
            }
            if (apiException.getSpanId() != null) {
                content.append("\n【spanId】：").append(apiException.getSpanId());
            }
            if (apiException.getThrowable() != null) {
                boolean isExceptionRemindType = apiException.getRemindType() == ApiExceptionRemindType.EXCEPTION_INFO;
                content.append("\n").append(isExceptionRemindType ? "⚠" : StringUtils.EMPTY).append("【异常信息】：").append(apiException.getThrowable().toString());
            }
            needMention |= apiException.getNeedAtSomeone();
        }

        Set<String> mentionedMobileList = needMention ? apiMonitorProperties.getExceptionApiMonitor().getReminders() : null;
        return new WeworkRobotTextMessageDTO(content.toString(), mentionedMobileList);
    }

}