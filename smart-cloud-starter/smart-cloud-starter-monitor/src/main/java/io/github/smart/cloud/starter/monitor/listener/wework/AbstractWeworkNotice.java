package io.github.smart.cloud.starter.monitor.listener.wework;

import io.github.smart.cloud.starter.monitor.component.ReminderComponent;
import io.github.smart.cloud.starter.monitor.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.Set;

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