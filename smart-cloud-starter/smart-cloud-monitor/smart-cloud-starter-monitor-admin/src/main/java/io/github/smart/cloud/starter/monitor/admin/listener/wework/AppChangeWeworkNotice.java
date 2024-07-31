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
package io.github.smart.cloud.starter.monitor.admin.listener.wework;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import io.github.smart.cloud.monitor.common.dto.wework.WeworkRobotMarkdownMessageDTO;
import io.github.smart.cloud.starter.monitor.admin.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.admin.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.admin.event.*;
import io.github.smart.cloud.utility.DateUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 监听服务状态变更，并企业微信发送通知
 *
 * @author collin
 * @date 2024-01-25
 */
@RequiredArgsConstructor
public class AppChangeWeworkNotice implements ApplicationListener<AbstractAppChangeEvent> {

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
        if (statusInfo.isDown() || statusInfo.isOffline()) {
            Object reason = getReason(statusInfo);
            if (reason != null) {
                content.append("**原因**: ").append(reason).append('\n');
            }
        }

        if (!(event instanceof MarkedOfflineEvent)) {
            // 提醒人
            String reminderParams = reminderComponent.getReminderParams(event.getName(), event);
            if (StringUtils.hasText(reminderParams)) {
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
        String STATUS = "status";
        String DETAILS = "details";

        String MESSAGE = "message";
        String ERROR = "error";
    }

}