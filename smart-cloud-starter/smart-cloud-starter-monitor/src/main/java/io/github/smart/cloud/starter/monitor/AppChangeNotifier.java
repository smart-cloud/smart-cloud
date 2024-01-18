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
import io.github.smart.cloud.starter.monitor.component.GitLabComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final GitLabComponent gitLabComponent;
    private final MonitorProperties monitorProperties;
    /**
     * 服务启动时间
     */
    private static final long START_UP_TS = System.currentTimeMillis();

    public AppChangeNotifier(InstanceRepository repository, RobotComponent robotComponent, GitLabComponent gitLabComponent, MonitorProperties monitorProperties) {
        super(repository);
        this.instanceRepository = repository;

        this.robotComponent = robotComponent;
        this.gitLabComponent = gitLabComponent;
        this.monitorProperties = monitorProperties;
    }

    @Override
    protected boolean shouldNotify(InstanceEvent event, Instance instance) {
        return event instanceof InstanceStatusChangedEvent;
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (System.currentTimeMillis() - START_UP_TS <= monitorProperties.getFilterEventTs()) {
                //服务启动时，90秒内的服务通知过滤掉
                return;
            }

            Registration registration = instance.getRegistration();
            StatusInfo statusInfo = instance.getStatusInfo();

            // 服务状态描述
            String state;
            if (statusInfo.isDown()) {
                state = "<font color=\\\"comment\\\">**健康检查没通过**</font>";
            } else if (statusInfo.isUp()) {
                state = "<font color=\\\"info\\\">**上线**</font>";
            } else if (statusInfo.isOffline()) {
                state = "<font color=\\\"warning\\\">**离线**</font>";
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
            content.append("**服务名**: ").append(registration.getName()).append("\n").append("**address**: ").append(registration.getServiceUrl()).append("\n").append("**状态**: ").append(state).append("\n").append("**在线实例数**: ").append(healthInstanceCountDesc);

            String apiUnHealthDetail = getApiUnHealthDetail(statusInfo);
            content.append(apiUnHealthDetail);

            // 提醒人
            String reminderParams = getReminderParams(registration.getName(), statusInfo, apiUnHealthDetail);
            content.append(reminderParams);

            robotComponent.sendWxworkNotice(getRobotKey(registration.getName()), content.toString());
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

    /**
     * 获取机器人key
     *
     * @param serviceName
     * @return
     */
    private String getRobotKey(String serviceName) {
        ServiceInfoProperties serviceInfoProperties = monitorProperties.getServiceInfos().get(serviceName);
        if (serviceInfoProperties == null) {
            return monitorProperties.getRobotKey();
        }

        return serviceInfoProperties.getRobotKey();
    }

    /**
     * 获取提醒人
     *
     * @param serviceName
     * @param statusInfo
     * @param apiUnHealthDetail
     * @return
     */
    private String getReminderParams(String serviceName, StatusInfo statusInfo, String apiUnHealthDetail) {
        if (!statusInfo.isDown() && !statusInfo.isOffline()) {
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

        if (StringUtils.isNotBlank(apiUnHealthDetail)) {
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

    private String generateReminders(Set<String> reminders) {
        StringBuilder sb = new StringBuilder(32);
        reminders.forEach(reminder -> sb.append("\n").append("<@").append(reminder).append(">"));
        return sb.toString();
    }

    static interface Constants {
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