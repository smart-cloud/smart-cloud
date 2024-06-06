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

import io.github.smart.cloud.starter.monitor.admin.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.admin.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.Set;

/**
 * 企业微信机器人通知父类
 *
 * @param <E>
 * @author collin
 * @date 2024-05-13
 */
@RequiredArgsConstructor
public abstract class AbstractWeworkNotice<E extends ApplicationEvent> implements ApplicationListener<E> {

    protected final RobotComponent robotComponent;
    protected final MonitorProperties monitorProperties;
    protected final ReminderComponent reminderComponent;

    /**
     * 获取提醒人
     *
     * @param serviceName
     * @return
     */
    protected String getReminderParams(String serviceName) {
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