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
package io.github.smart.cloud.starter.monitor.schedule;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import io.github.smart.cloud.starter.monitor.event.notice.OfflineNoticeEvent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationEventPublisher;

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
public class OfflineCheckSchedule implements SmartInitializingSingleton, DisposableBean {

    private final InstanceRepository instanceRepository;
    private final MonitorProperties monitorProperties;
    private final ApplicationEventPublisher applicationEventPublisher;
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
     * 检查服务在线实例数
     */
    private void checkOffline() {
        if (OFF_LINE_SERVICES.isEmpty()) {
            return;
        }

        OFF_LINE_SERVICES.forEach(name -> {
            Long healthInstanceCount = instanceRepository.findByName(name).filter(item -> item.getStatusInfo().isUp()).count().share().block();
            if (healthInstanceCount > 0 || monitorProperties.getExcludeOfflineCheckServices().contains(name)) {
                OFF_LINE_SERVICES.remove(name);
            } else {
                applicationEventPublisher.publishEvent(new OfflineNoticeEvent(this, name));
            }
        });
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