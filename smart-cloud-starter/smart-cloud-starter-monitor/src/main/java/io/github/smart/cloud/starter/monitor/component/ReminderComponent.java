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
package io.github.smart.cloud.starter.monitor.component;

import io.github.smart.cloud.starter.monitor.event.AbstractAppChangeEvent;
import io.github.smart.cloud.starter.monitor.event.UpEvent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 服务提醒信息
 *
 * @author collin
 * @date 2024-01-25
 */
@Slf4j
@RequiredArgsConstructor
public class ReminderComponent {

    private final GitLabComponent gitLabComponent;
    private final MonitorProperties monitorProperties;

    /**
     * 获取提醒人
     *
     * @param serviceName
     * @param event
     * @param apiUnHealthDetail
     * @return
     */
    public String getReminderParams(String serviceName, AbstractAppChangeEvent event, String apiUnHealthDetail) {
        if (event instanceof UpEvent) {
            return StringUtils.EMPTY;
        }

        ServiceInfoProperties serviceInfoProperties = monitorProperties.getServiceInfos().get(serviceName);
        if (serviceInfoProperties == null) {
            return StringUtils.EMPTY;
        }
        Set<String> reminders = serviceInfoProperties.getReminders();
        if (CollectionUtils.isEmpty(reminders)) {
            return StringUtils.EMPTY;
        }

        if (event.getHealthInstanceCount() <= 0) {
            return generateReminders(reminders);
        }

        if (StringUtils.isNotBlank(apiUnHealthDetail)) {
            return generateReminders(reminders);
        }

        if (!gitLabComponent.enable()) {
            return generateReminders(reminders);
        }

        // 查询最近是否有tag记录
        Long lastTagCommittedTs = null;
        try {
            lastTagCommittedTs = gitLabComponent.getLastTagCreateAtTs(serviceName);
            if (lastTagCommittedTs == null || lastTagCommittedTs == 0) {
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            log.error("fetch git info error|serviceName={}", serviceName, e);
            return StringUtils.EMPTY;
        }

        if (System.currentTimeMillis() - lastTagCommittedTs < serviceInfoProperties.getRemindTagMinDiffTs()) {
            return StringUtils.EMPTY;
        }

        return generateReminders(reminders);
    }

    /**
     * 获取提醒人
     *
     * @param reminders
     * @return
     */
    public String generateReminders(Set<String> reminders) {
        StringBuilder sb = new StringBuilder(32);
        reminders.forEach(reminder -> sb.append("\n<@").append(reminder).append('>'));
        return sb.toString();
    }

}
