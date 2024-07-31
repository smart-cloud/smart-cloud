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
package io.github.smart.cloud.starter.monitor.admin.listener.wework;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.monitor.common.dto.wework.WeworkRobotMarkdownMessageDTO;
import io.github.smart.cloud.starter.monitor.admin.component.RobotComponent;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.event.MetricAlertEvent;
import io.github.smart.cloud.utility.DateUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;

/**
 * 服务指标监控企业微信通知
 *
 * @author collin
 * @date 2024-07-28
 */
@RequiredArgsConstructor
public class MetricsAlertListener implements ApplicationListener<MetricAlertEvent> {

    private final RobotComponent robotComponent;

    @Override
    public void onApplicationEvent(MetricAlertEvent event) {
        Instance instance = event.getInstance();
        MetricCheckResultDTO metricCheckResult = event.getMetricCheckResult();

        String serviceName = instance.getRegistration().getName();
        StringBuilder content = new StringBuilder(128);
        content.append("**时间**：").append(DateUtil.getCurrentDateTime()).append("\n")
                .append("**服务**：").append(instance.getRegistration().getName()).append("\n")
                .append("**地址**：").append(instance.getRegistration().getServiceUrl()).append("\n")
                .append("**指标**：").append(event.getInstanceMetric().getDesc()).append("-").append(metricCheckResult.getMetricCheckStatus().getDesc()).append("\n")
                .append("**信息**：").append(metricCheckResult.getAlertDesc());
        String messaeg = JacksonUtil.toJson(new WeworkRobotMarkdownMessageDTO(content.toString()));

        robotComponent.sendWxworkNotice(robotComponent.getRobotKey(serviceName), messaeg);
    }

}