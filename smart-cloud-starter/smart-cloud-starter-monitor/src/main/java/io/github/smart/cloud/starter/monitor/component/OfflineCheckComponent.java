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

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 检查在线实例数为0的服务
 *
 * @author collin
 * @date 2024-01-25
 */
@RequiredArgsConstructor
public class OfflineCheckComponent implements SmartInitializingSingleton, DisposableBean {

    private final InstanceRepository instanceRepository;
    private final RobotComponent robotComponent;
    private final MonitorProperties monitorProperties;
    private final ReminderComponent reminderComponent;
    /**
     * 离线服务名
     */
    private static final Set<String> OFF_LINE_SERVICES = new CopyOnWriteArraySet<>();
    private ScheduledExecutorService checkOfflineSchedule = null;

    /**
     * 添加离线服务
     *
     * @param name
     */
    public void add(String name) {
        OFF_LINE_SERVICES.add(name);
    }

    /**
     * 检查实现服务在线实例数
     */
    public void checkOffline() {
        if (OFF_LINE_SERVICES.isEmpty()) {
            return;
        }

        OFF_LINE_SERVICES.forEach(name -> {
            Long healthInstanceCount = instanceRepository.findByName(name)
                    .filter(item -> item.getStatusInfo().isUp())
                    .count().share().block();
            if (healthInstanceCount > 0 || monitorProperties.getExcludeServices().contains(name)) {
                OFF_LINE_SERVICES.remove(name);
            } else {
                String reminders = getReminderParams(name);
                StringBuilder content = new StringBuilder(64);
                content.append("**").append(name).append("**服务<font color=\\\"warning\\\">**在线实例数为0**</font>");
                if (StringUtils.isNotBlank(reminders)) {
                    content.append(reminders);
                }
                robotComponent.sendWxworkNotice(robotComponent.getRobotKey(name), content.toString());
            }
        });
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

    @Override
    public void afterSingletonsInstantiated() {
        checkOfflineSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("check-off-line-service"));
        checkOfflineSchedule.scheduleWithFixedDelay(this::checkOffline, monitorProperties.getCheckOfflineTs(), monitorProperties.getCheckOfflineTs(), TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        if (checkOfflineSchedule != null) {
            checkOfflineSchedule.shutdown();
        }
    }

}