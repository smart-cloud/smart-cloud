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
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import io.github.smart.cloud.starter.monitor.component.OfflineCheckComponent;
import io.github.smart.cloud.starter.monitor.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务状态变更监听
 *
 * @author collin
 * @date 2020-09-15
 */
@Slf4j
public class AppChangeNotifier extends AbstractStatusChangeNotifier {

    private final InstanceRepository instanceRepository;
    private final RobotComponent robotComponent;
    private final ReminderComponent reminderComponent;
    private final OfflineCheckComponent offlineCheckComponent;
    private final MonitorProperties monitorProperties;
    /**
     * 服务启动时间
     */
    private static final long START_UP_TS = System.currentTimeMillis();

    public AppChangeNotifier(InstanceRepository instanceRepository, RobotComponent robotComponent, ReminderComponent reminderComponent, final OfflineCheckComponent offlineCheckComponent,
                             MonitorProperties monitorProperties) {
        super(instanceRepository);
        this.instanceRepository = instanceRepository;

        this.robotComponent = robotComponent;
        this.reminderComponent = reminderComponent;
        this.offlineCheckComponent = offlineCheckComponent;
        this.monitorProperties = monitorProperties;
    }

    @Override
    protected boolean shouldNotify(InstanceEvent event, Instance instance) {
        return event instanceof InstanceStatusChangedEvent;
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            Registration registration = instance.getRegistration();
            if (monitorProperties.getExcludeServices().contains(registration.getName())) {
                return;
            }

            StatusInfo statusInfo = instance.getStatusInfo();
            if (System.currentTimeMillis() - START_UP_TS <= monitorProperties.getFilterEventTs() && statusInfo.isUp()) {
                //服务启动时，短时间内的服务上线通知过滤掉
                return;
            }

            // 服务状态描述
            String state;
            if (statusInfo.isDown()) {
                state = "<font color=\\\"comment\\\">**健康检查没通过**</font>";
            } else if (statusInfo.isUp()) {
                state = "<font color=\\\"info\\\">**上线**</font>";
            } else if (statusInfo.isOffline()) {
                state = "<font color=\\\"warning\\\">**离线**</font>";
                offlineCheckComponent.add(registration.getName());
            } else if (statusInfo.isUnknown()) {
                state = "<font color=\\\"comment\\\">**未知异常**</font>";
            } else {
                state = "**unknow**";
            }

            if (statusInfo.isDown() || statusInfo.isOffline()) {
                log.warn("{}==>{}", registration.getName(), JacksonUtil.toJson(instance));
            } else {
                log.info("{}==>{}", registration.getName(), statusInfo.getStatus());
            }

            Long healthInstanceCount = instanceRepository.findByName(registration.getName())
                    .filter(item -> item.getStatusInfo().isUp())
                    .count()
                    .share()
                    .block();
            // 在线实例数
            String healthInstanceCountDesc = healthInstanceCount > 0 ? String.valueOf(healthInstanceCount) : "<font color=\\\"warning\\\">**0**</font>";

            StringBuilder content = new StringBuilder(64);
            content.append("**服务名**: ").append(registration.getName()).append("\n")
                    .append("**address**: ").append(registration.getServiceUrl()).append("\n")
                    .append("**状态**: ").append(state).append("\n")
                    .append("**在线实例数**: ").append(healthInstanceCountDesc);

            String apiUnHealthDetail = getApiUnHealthDetail(statusInfo);
            content.append(apiUnHealthDetail);

            // 提醒人
            String reminderParams = reminderComponent.getReminderParams(registration.getName(), statusInfo, apiUnHealthDetail);
            content.append(reminderParams);

            robotComponent.sendWxworkNotice(robotComponent.getRobotKey(registration.getName()), content.toString());
        });
    }

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
        unHealthContent.append("\n<font color=\\\"warning\\\">**非健康接口(近5分钟)**</font>:");
        int apiIndex = 0;
        for (Map<String, Object> unHealthInfo : unHealthInfos) {
            unHealthContent.append("\n\n>**接口").append(++apiIndex).append("**: ").append(unHealthInfo.get(Constants.NAME))
                    .append("\n**请求总数**: ").append(unHealthInfo.get(Constants.TOTAL))
                    .append("\n>**失败数**: ").append(unHealthInfo.get(Constants.FAIL_COUNT))
                    .append("\n>**失败率**: ").append(unHealthInfo.get(Constants.FAIL_RATE));
        }
        unHealthContent.append("\n");
        return unHealthContent.toString();
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
    }

}