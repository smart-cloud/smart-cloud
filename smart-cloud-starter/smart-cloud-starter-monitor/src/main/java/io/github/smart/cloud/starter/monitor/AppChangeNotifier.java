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
package io.github.smart.cloud.starter.monitor;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceDeregisteredEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import io.github.smart.cloud.starter.monitor.event.*;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import reactor.core.publisher.Mono;

/**
 * 服务状态变更监听
 *
 * @author collin
 * @date 2020-09-15
 */
@Slf4j
public class AppChangeNotifier extends AbstractStatusChangeNotifier implements ApplicationListener<ApplicationReadyEvent> {

    private final InstanceRepository instanceRepository;
    private final MonitorProperties monitorProperties;
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 服务启动时间
     */
    private Long monitorStartUpTs;

    public AppChangeNotifier(InstanceRepository instanceRepository, MonitorProperties monitorProperties, ApplicationEventPublisher applicationEventPublisher) {
        super(instanceRepository);
        this.instanceRepository = instanceRepository;

        this.monitorProperties = monitorProperties;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.monitorStartUpTs = System.currentTimeMillis();
    }

    @Override
    protected boolean shouldNotify(InstanceEvent event, Instance instance) {
        return event instanceof InstanceStatusChangedEvent || event instanceof InstanceDeregisteredEvent;
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            Registration registration = instance.getRegistration();
            if (monitorProperties.getExcludeServices().contains(registration.getName())) {
                return;
            }

            StatusInfo statusInfo = instance.getStatusInfo();
            if (filter(statusInfo)) {
                //服务启动时，60秒内的服务上线通知过滤掉
                return;
            }

            AbstractAppChangeEvent appChangeEvent = null;
            if (statusInfo.isDown()) {
                appChangeEvent = new DownEvent(this);
            } else if (statusInfo.isUp()) {
                appChangeEvent = new UpEvent(this);
            } else if (statusInfo.isOffline()) {
                appChangeEvent = new OfflineEvent(this);
            } else if (statusInfo.isUnknown()) {
                // 注册中心人工标记下线
                if (event instanceof InstanceDeregisteredEvent) {
                    appChangeEvent = new MarkedOfflineEvent(this);
                } else {
                    appChangeEvent = new UnknownEvent(this);
                }
            } else {
                appChangeEvent = new UnknownEvent(this);
            }

            if (isOffline(appChangeEvent)) {
                log.warn("{}==>{}", registration.getName(), JacksonUtil.toJson(instance));
            } else {
                log.info("{}==>{}", registration.getName(), statusInfo.getStatus());
            }

            Long healthInstanceCount = instanceRepository.findByName(registration.getName())
                    .filter(item -> item.getStatusInfo().isUp())
                    .count()
                    .share()
                    .block();

            appChangeEvent.setName(registration.getName());
            appChangeEvent.setHealthInstanceCount(healthInstanceCount);
            appChangeEvent.setUrl(registration.getServiceUrl());
            appChangeEvent.setStatusInfo(statusInfo);
            applicationEventPublisher.publishEvent(appChangeEvent);
        });
    }

    /**
     * 是否需要过滤事件
     *
     * @param statusInfo
     * @return
     */
    private boolean filter(StatusInfo statusInfo) {
        return statusInfo.isUp() && (monitorStartUpTs == null || System.currentTimeMillis() - monitorStartUpTs <= monitorProperties.getFilterEventTs());
    }

    /**
     * 是否是离线
     *
     * @param appChangeEvent
     * @return
     */
    private boolean isOffline(AbstractAppChangeEvent appChangeEvent) {
        return appChangeEvent instanceof DownEvent || appChangeEvent instanceof OfflineEvent || appChangeEvent instanceof MarkedOfflineEvent;
    }

}