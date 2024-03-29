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

import io.github.smart.cloud.starter.monitor.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.event.offline.OfflineNoticeEvent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;

import java.util.Set;

/**
 * 在线实例为0时，企业微信通知
 *
 * @author collin
 * @date 2024-02-23
 */
@RequiredArgsConstructor
public class OfflineWeworkNotice implements ApplicationListener<OfflineNoticeEvent> {

    private final RobotComponent robotComponent;
    private final MonitorProperties monitorProperties;
    private final ReminderComponent reminderComponent;

    @Override
    public void onApplicationEvent(OfflineNoticeEvent event) {
        String name = event.getName();
        String reminders = getReminderParams(name);
        StringBuilder content = new StringBuilder(64);
        content.append("**").append(name).append("**服务<font color=\\\"warning\\\">**在线实例数为0**</font>");
        if (StringUtils.isNotBlank(reminders)) {
            content.append(reminders);
        }
        robotComponent.sendWxworkNotice(robotComponent.getRobotKey(name), content.toString());
    }

    /**
     * 获取提醒人
     *
     * @param serviceName
     * @return
     */
    private String getReminderParams(String serviceName) {
        ServiceInfoProperties projectProperties = monitorProperties.getServiceInfos().get(serviceName);
        if (projectProperties == null) {
            return StringUtils.EMPTY;
        }
        Set<String> reminders = projectProperties.getReminders();
        if (CollectionUtils.isEmpty(reminders)) {
            return StringUtils.EMPTY;
        }

        return reminderComponent.generateReminders(reminders);
    }

}