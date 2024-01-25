package io.github.smart.cloud.starter.monitor.component;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;
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

    public String getReminderParams(String serviceName, StatusInfo statusInfo, String apiUnHealthDetail) {
        if (!statusInfo.isDown() && !statusInfo.isOffline()) {
            return StringUtils.EMPTY;
        }

        ServiceInfoProperties projectProperties = monitorProperties.getServiceInfos().get(serviceName);
        if (projectProperties == null) {
            return StringUtils.EMPTY;
        }
        Set<String> reminders = projectProperties.getReminders();
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

        if (System.currentTimeMillis() - lastTagCommittedTs < projectProperties.getRemindTagMinDiffTs()) {
            return StringUtils.EMPTY;
        }

        return generateReminders(reminders);
    }

    public String generateReminders(Set<String> reminders) {
        StringBuilder sb = new StringBuilder(32);
        reminders.forEach(reminder -> sb.append("\n").append("<@").append(reminder).append(">"));
        return sb.toString();
    }

}
