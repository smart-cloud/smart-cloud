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
package io.github.smart.cloud.starter.monitor.admin.schedule;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import io.github.smart.cloud.starter.monitor.admin.event.notice.ServiceNodeCountCheckNoticeEvent;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 服务节点数检查
 *
 * @author collin
 * @date 2024-05-06
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceNodeCountCheckSchedule implements SmartInitializingSingleton, DisposableBean {

    private final InstanceRepository instanceRepository;
    private final MonitorProperties monitorProperties;
    private final ApplicationEventPublisher applicationEventPublisher;
    private ScheduledExecutorService checkServiceNodeCountSchedule;

    @Override
    public void afterSingletonsInstantiated() {
        checkServiceNodeCountSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("check-service-node-count"));
        checkServiceNodeCountSchedule.scheduleWithFixedDelay(this::checkServiceNodeCount, monitorProperties.getCheckServiceNodeCountTs(),
                monitorProperties.getCheckServiceNodeCountTs(), TimeUnit.SECONDS);
    }

    private void checkServiceNodeCount() {
        Map<String, List<Instance>> instanceOnlineInfo = instanceRepository.findAll()
                .collectList()
                .map(instances -> instances.stream().collect(Collectors.groupingBy(instance -> instance.getRegistration().getName()))).share().block();
        if (MapUtils.isEmpty(instanceOnlineInfo)) {
            log.warn("instance online info is empty");
            return;
        }

        Map<String, ServiceInfoProperties> serviceConfigInfo = monitorProperties.getServiceInfos();
        if (MapUtils.isEmpty(serviceConfigInfo)) {
            log.warn("service config Info info is empty");
            return;
        }

        instanceOnlineInfo.forEach((name, instances) -> {
            int currentNodeCount = instances.size();
            if (currentNodeCount == 0) {
                applicationEventPublisher.publishEvent(new ServiceNodeCountCheckNoticeEvent(this, name, currentNodeCount));
                return;
            }

            ServiceInfoProperties serviceInfoProperties = serviceConfigInfo.get(name);
            if (serviceInfoProperties == null) {
                return;
            }

            Integer nodeCount = serviceInfoProperties.getNodeCount();
            if (nodeCount == null) {
                return;
            }

            if (currentNodeCount < nodeCount) {
                applicationEventPublisher.publishEvent(new ServiceNodeCountCheckNoticeEvent(this, name, currentNodeCount));
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        if (checkServiceNodeCountSchedule != null) {
            checkServiceNodeCountSchedule.shutdown();
        }
    }

}