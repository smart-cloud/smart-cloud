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
package io.github.smart.cloud.starter.monitor.listener;

import com.alibaba.nacos.common.utils.MapUtil;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import io.github.smart.cloud.starter.monitor.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.event.*;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.utility.DateUtil;
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
        String healthInstanceCountDesc = healthInstanceCount > 0 ? String.valueOf(healthInstanceCount) : "<font color=\\\"warning\\\">**0**</font>";

        StringBuilder content = new StringBuilder(128);
        content.append("**时间**：").append(DateUtil.getCurrentDateTime()).append("\n")
                .append("**服务**: ").append(event.getName()).append("\n")
                .append("**地址**: ").append(event.getUrl()).append("\n")
                .append("**状态**: ").append(getState(event)).append("\n")
                .append("**在线实例数**: ").append(healthInstanceCountDesc).append("\n");

        // 接口健康信息
        StatusInfo statusInfo = event.getStatusInfo();
        String apiUnHealthDetail = getApiUnHealthDetail(statusInfo);
        if (StringUtils.isNotBlank(apiUnHealthDetail)) {
            content.append(apiUnHealthDetail);
        } else if (statusInfo.isDown() || statusInfo.isOffline()) {
            Object reason = getReason(statusInfo);
            if (reason != null) {
                content.append("**原因**: ").append(reason).append("\n");
            }
        }

        if (!(event instanceof MarkedOfflineEvent)) {
            // 提醒人
            String reminderParams = reminderComponent.getReminderParams(event.getName(), event, apiUnHealthDetail);
            if (StringUtils.isNotBlank(reminderParams)) {
                content.append(reminderParams);
            }
        }

        robotComponent.sendWxworkNotice(robotComponent.getRobotKey(event.getName()), content.toString());
    }

    /**
     * 获取服务状态描述
     *
     * @param event
     * @return
     */
    private String getState(AbstractAppChangeEvent event) {
        if (event instanceof DownEvent) {
            return "<font color=\\\"comment\\\">**健康检查没通过**</font>";
        } else if (event instanceof UpEvent) {
            return "<font color=\\\"info\\\">**上线**</font>";
        } else if (event instanceof OfflineEvent) {
            return "<font color=\\\"warning\\\">**离线**</font>";
        } else if (event instanceof MarkedOfflineEvent) {
            return "<font color=\\\"comment\\\">**被人工标记下线**</font>";
        } else if (event instanceof UnknownEvent) {
            return "<font color=\\\"comment\\\">**未知异常**</font>";
        }

        return "**unknow**";
    }

    /**
     * 获取自定义健康检查信息
     *
     * @param statusInfo
     * @return
     */
    private String getApiUnHealthDetail(StatusInfo statusInfo) {
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

        Map<String, Object> apiUnHealthDetail = (Map<String, Object>) apiHealthInfo.get(Constants.DETAILS);
        if (MapUtils.isEmpty(apiUnHealthDetail)) {
            return StringUtils.EMPTY;
        }

        List<Map<String, Object>> unHealthInfos = (ArrayList<Map<String, Object>>) apiUnHealthDetail.get(Constants.UN_HEALTH_INFOS);
        if (CollectionUtils.isEmpty(unHealthInfos)) {
            return StringUtils.EMPTY;
        }

        StringBuilder unHealthContent = new StringBuilder(64);
        unHealthContent.append("<font color=\\\"warning\\\">**非健康接口(近").append(monitorProperties.getExceptionApiCheckInterval()).append("分钟)**</font>:");
        int apiIndex = 0;
        for (Map<String, Object> unHealthInfo : unHealthInfos) {
            unHealthContent.append("\n\n>**接口").append(++apiIndex).append("**: ").append(unHealthInfo.get(Constants.NAME))
                    .append("\n>**请求总数**: ").append(unHealthInfo.get(Constants.TOTAL))
                    .append("\n>**失败数**: ").append(unHealthInfo.get(Constants.FAIL_COUNT))
                    .append("\n>**失败率**: ").append(unHealthInfo.get(Constants.FAIL_RATE));
        }
        unHealthContent.append("\n");
        return unHealthContent.toString();
    }

    /**
     * 获取服务离线、下线原因
     *
     * @param statusInfo
     * @return
     */
    private Object getReason(StatusInfo statusInfo) {
        Map<String, Object> details = statusInfo.getDetails();
        if (MapUtil.isEmpty(details)) {
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
        String UN_HEALTH_INFOS = "unHealthInfos";
        String NAME = "name";
        String TOTAL = "total";
        String FAIL_COUNT = "failCount";
        String FAIL_RATE = "failRate";
        String MESSAGE = "message";
        String ERROR = "error";
    }

}