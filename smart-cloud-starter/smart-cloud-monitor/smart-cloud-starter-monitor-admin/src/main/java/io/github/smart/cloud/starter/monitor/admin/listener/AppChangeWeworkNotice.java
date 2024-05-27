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
package io.github.smart.cloud.starter.monitor.admin.listener;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.monitor.common.dto.wework.WeworkRobotMarkdownMessageDTO;
import io.github.smart.cloud.starter.monitor.admin.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.admin.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.admin.event.*;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.utility.DateUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听服务状态变更，并企业微信发送通知
 *
 * @author collin
 * @date 2024-01-25
 */
@RequiredArgsConstructor
public class AppChangeWeworkNotice implements ApplicationListener<AbstractAppChangeEvent> {

    private final MonitorProperties monitorProperties;
    private final RobotComponent robotComponent;
    private final ReminderComponent reminderComponent;

    @Override
    public void onApplicationEvent(AbstractAppChangeEvent event) {
        // 在线实例数
        Long healthInstanceCount = event.getHealthInstanceCount();
        String healthInstanceCountDesc = healthInstanceCount > 0 ? String.valueOf(healthInstanceCount) : "<font color=\"warning\">**0**</font>";

        StringBuilder content = new StringBuilder(128);
        content.append("**时间**：").append(DateUtil.getCurrentDateTime()).append('\n')
                .append("**服务**: ").append(event.getName()).append('\n')
                .append("**地址**: ").append(event.getUrl()).append('\n')
                .append("**状态**: ").append(getState(event)).append('\n')
                .append("**在线实例数**: ").append(healthInstanceCountDesc).append('\n');

        // 接口健康信息
        StatusInfo statusInfo = event.getStatusInfo();
        String apiExceptionInfo = getApiExceptionInfo(statusInfo);
        if (StringUtils.isNotBlank(apiExceptionInfo)) {
            content.append(apiExceptionInfo);
        } else if (statusInfo.isDown() || statusInfo.isOffline()) {
            Object reason = getReason(statusInfo);
            if (reason != null) {
                content.append("**原因**: ").append(reason).append('\n');
            }
        }

        if (!(event instanceof MarkedOfflineEvent)) {
            // 提醒人
            String reminderParams = reminderComponent.getReminderParams(event.getName(), event, apiExceptionInfo);
            if (StringUtils.isNotBlank(reminderParams)) {
                content.append(reminderParams);
            }
        }

        String robotMessage = JacksonUtil.toJson(new WeworkRobotMarkdownMessageDTO(content.toString()));
        robotComponent.sendWxworkNotice(robotComponent.getRobotKey(event.getName()), robotMessage);
    }

    /**
     * 获取服务状态描述
     *
     * @param event
     * @return
     */
    private String getState(AbstractAppChangeEvent event) {
        if (event instanceof DownEvent) {
            return "<font color=\"comment\">**健康检查没通过**</font>";
        } else if (event instanceof UpEvent) {
            return "<font color=\"info\">**上线**</font>";
        } else if (event instanceof OfflineEvent) {
            return "<font color=\"warning\">**离线**</font>";
        } else if (event instanceof MarkedOfflineEvent) {
            return "<font color=\"comment\">**被人工标记下线**</font>";
        } else if (event instanceof UnknownEvent) {
            return "<font color=\"comment\">**未知异常**</font>";
        }

        return "**unknow**";
    }

    /**
     * 获取自定义健康检查信息
     *
     * @param statusInfo
     * @return
     */
    private String getApiExceptionInfo(StatusInfo statusInfo) {
        if (!statusInfo.isDown()) {
            return StringUtils.EMPTY;
        }

        Map<String, Object> detail = statusInfo.getDetails();
        if (MapUtils.isEmpty(detail)) {
            return StringUtils.EMPTY;
        }

        Map<String, Object> apiHealthInfo = (Map<String, Object>) detail.get(Constants.API);
        if (MapUtils.isEmpty(apiHealthInfo)) {
            return StringUtils.EMPTY;
        }

        if (!StatusInfo.STATUS_DOWN.equals(apiHealthInfo.get(Constants.STATUS))) {
            return StringUtils.EMPTY;
        }

        Map<String, Object> apiExceptionDetail = (Map<String, Object>) apiHealthInfo.get(Constants.DETAILS);
        if (MapUtils.isEmpty(apiExceptionDetail)) {
            return StringUtils.EMPTY;
        }

        List<Map<String, Object>> apiExceptions = (ArrayList<Map<String, Object>>) apiExceptionDetail.get(Constants.API_EXCEPTIONS);
        if (CollectionUtils.isEmpty(apiExceptions)) {
            return StringUtils.EMPTY;
        }

        StringBuilder apiExceptionContent = new StringBuilder(128);
        apiExceptionContent.append("<font color=\"warning\">**非健康接口(近").append(monitorProperties.getExceptionApiCheckInterval()).append("分钟)**</font>:");
        int apiIndex = 0;
        for (Map<String, Object> apiException : apiExceptions) {
            apiExceptionContent.append("\n\n>**接口").append(++apiIndex).append("**: ").append(apiException.get(Constants.NAME))
                    .append("\n>**请求总数**: ").append(apiException.get(Constants.TOTAL))
                    .append("\n>**失败数**: ").append(apiException.get(Constants.FAIL_COUNT))
                    .append("\n>**失败率**: ").append(apiException.get(Constants.FAIL_RATE));
            String failMessage = (String) apiException.get(Constants.MESSAGE);
            if (failMessage != null) {
                if (failMessage.contains(SymbolConstant.DOUBLE_QUOTE)) {
                    failMessage = StringUtils.remove(failMessage, SymbolConstant.DOUBLE_QUOTE);
                }
                apiExceptionContent.append("\n>**异常信息**：").append(failMessage);
            }
        }
        apiExceptionContent.append('\n');
        return apiExceptionContent.toString();
    }

    /**
     * 获取服务离线、下线原因
     *
     * @param statusInfo
     * @return
     */
    private Object getReason(StatusInfo statusInfo) {
        Map<String, Object> details = statusInfo.getDetails();
        if (details == null || details.isEmpty()) {
            return null;
        }
        if (statusInfo.isDown()) {
            for (Map.Entry<String, Object> entry : details.entrySet()) {
                Object v = entry.getValue();
                Object message = getMessage(v);
                if (message != null) {
                    return message;
                }
            }
        } else {
            // offline
            Object message = details.get(Constants.MESSAGE);
            if (message != null) {
                return message;
            }
        }

        return null;
    }

    /**
     * 解析离线、下线原因
     *
     * @param o
     * @return
     */
    private Object getMessage(Object o) {
        if (!(o instanceof LinkedHashMap)) {
            return null;
        }

        LinkedHashMap v = (LinkedHashMap) o;
        if (v.containsKey(Constants.ERROR)) {
            return v.get(Constants.ERROR);
        } else if (v.containsKey(Constants.STATUS)) {
            Object status = v.get(Constants.STATUS);
            if (status == null) {
                return null;
            } else if (StatusInfo.STATUS_DOWN.equals(status)) {
                Object details = v.get(Constants.DETAILS);
                if (details == null) {
                    return null;
                } else {
                    return getMessage(details);
                }
            }
        }
        return null;
    }

    interface Constants {
        String API = "api";
        String STATUS = "status";
        String DETAILS = "details";
        String API_EXCEPTIONS = "apiExceptions";
        String NAME = "name";
        String TOTAL = "total";
        String FAIL_COUNT = "failCount";
        String FAIL_RATE = "failRate";

        String MESSAGE = "message";
        String ERROR = "error";
    }

}