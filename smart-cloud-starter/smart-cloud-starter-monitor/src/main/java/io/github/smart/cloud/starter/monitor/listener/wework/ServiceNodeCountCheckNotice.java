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
package io.github.smart.cloud.starter.monitor.listener.wework;

import io.github.smart.cloud.starter.monitor.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.event.notice.ServiceNodeCountCheckNoticeEvent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * 在线实例为0时，企业微信通知
 *
 * @author collin
 * @date 2024-02-23
 */
public class ServiceNodeCountCheckNotice extends AbstractWeworkNotice<ServiceNodeCountCheckNoticeEvent> {

    public ServiceNodeCountCheckNotice(RobotComponent robotComponent, MonitorProperties monitorProperties, ReminderComponent reminderComponent) {
        super(robotComponent, monitorProperties, reminderComponent);
    }

    @Override
    public void onApplicationEvent(ServiceNodeCountCheckNoticeEvent event) {
        String name = event.getName();
        String reminders = getReminderParams(name);
        StringBuilder content = new StringBuilder(64);
        content.append("**服务**: ").append(name).append('\n');

        ServiceInfoProperties serviceInfoProperties = monitorProperties.getServiceInfos().get(name);
        if (serviceInfoProperties != null) {
            Integer nodeCount = serviceInfoProperties.getNodeCount();
            if (nodeCount != null) {
                content.append("**期望实例数**: ").append(nodeCount).append('\n');
            }
        }
        content.append("**当前实例数**: <font color=\\\"warning\\\">").append(event.getNodeCount()).append("</font>\n");
        if (StringUtils.isNotBlank(reminders)) {
            content.append(reminders);
        }
        robotComponent.sendWxworkNotice(robotComponent.getRobotKey(name), content.toString());
    }

}