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
package io.github.smart.cloud.starter.monitor.admin.component.metrics.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.starter.monitor.admin.component.metrics.IInstanceMetricsMonitorComponent;
import io.github.smart.cloud.starter.monitor.admin.dto.MetricCheckResultDTO;
import io.github.smart.cloud.starter.monitor.admin.enums.InstanceMetric;
import io.github.smart.cloud.starter.monitor.admin.enums.MetricCheckStatus;
import io.github.smart.cloud.starter.monitor.admin.event.MetricAlertEvent;
import io.github.smart.cloud.starter.monitor.admin.properties.MetricItemAlertProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.admin.properties.ServiceInfoProperties;
import io.github.smart.cloud.starter.monitor.admin.util.ActuatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * tomcat指标监控
 *
 * @author collin
 * @date 2024-09-12
 */
@Slf4j
@RequiredArgsConstructor
public class TomcatMetricMonitorComponent implements IInstanceMetricsMonitorComponent<BigDecimal> {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper objectMapper;
    private final MonitorProperties monitorProperties;

    @Override
    public InstanceMetric getInstanceMetric() {
        return InstanceMetric.TOMCAT;
    }

    @Override
    public MetricCheckResultDTO check(Instance instance) throws IOException {
        // 查询指标：tomcat.threads.config.max、tomcat.threads.busy
        Integer tomcatThreadsConfigMax = getTomcatMetric(instance, "tomcat.threads.config.max");
        if (tomcatThreadsConfigMax == null) {
            return MetricCheckResultDTO.ok();
        }
        Integer tomcatThreadsbusy = getTomcatMetric(instance, "tomcat.threads.busy");
        if (tomcatThreadsbusy == null) {
            return MetricCheckResultDTO.ok();
        }
        if (BigDecimal.valueOf(tomcatThreadsbusy).compareTo(BigDecimal.valueOf(tomcatThreadsConfigMax).multiply(getThreshold(instance.getRegistration().getName()))) >= 0) {
            String alertDesc = String.format("tomcat活动线程[%d]已超过预警值[%d]", tomcatThreadsbusy, tomcatThreadsConfigMax);
            return MetricCheckResultDTO.error(MetricCheckStatus.AVAILABLE_RESOURCE_ALERT, alertDesc);
        }

        return MetricCheckResultDTO.ok();
    }

    @Override
    public boolean alert(Instance instance) throws IOException {
        MetricCheckResultDTO metricCheckResult = check(instance);
        MetricCheckStatus metricCheckStatus = metricCheckResult.getMetricCheckStatus();
        if (metricCheckStatus == MetricCheckStatus.OK) {
            return false;
        }

        MetricAlertEvent metricAlert = new MetricAlertEvent(this);
        metricAlert.setInstance(instance);
        metricAlert.setInstanceMetric(getInstanceMetric());
        metricAlert.setMetricCheckResult(metricCheckResult);
        applicationEventPublisher.publishEvent(metricAlert);
        return true;
    }

    @Override
    public void truncateHistory() {
        // do nothing
    }

    private Integer getTomcatMetric(Instance instance, String metricName) throws IOException {
        String response = ActuatorUtil.sendGetRequest(instance, metricName);
        if (response == null || ObjectUtils.isEmpty(response)) {
            return null;
        }

        try {
            JsonNode responseNode = objectMapper.readTree(response);
            if (responseNode == null) {
                return null;
            }
            JsonNode measurementsNodes = responseNode.get("measurements");
            if (measurementsNodes == null || measurementsNodes.isEmpty()) {
                return null;
            }
            JsonNode valueNode = ActuatorUtil.parseValueNode(measurementsNodes, "VALUE");
            if (valueNode == null) {
                return null;
            }

            return valueNode.asInt();
        } catch (JsonProcessingException e) {
            log.error("parse json error|response={}", response, e);
        }
        return null;
    }

    @Override
    public Integer getPreHeatHour(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<BigDecimal> metricItemAlert = serviceInfos.get(serviceName).getMetric().getTomcat();
            if (metricItemAlert.getPreHeatHour() != null) {
                return metricItemAlert.getPreHeatHour();
            }
        }

        return monitorProperties.getMetric().getTomcat().getPreHeatHour();
    }

    @Override
    public Integer getKeepIncreasingCount(String serviceName) {
        // do nothing
        return null;
    }

    @Override
    public BigDecimal getKeepIncreasingSpeedThreshold(String serviceName) {
        // do nothing
        return null;
    }

    @Override
    public BigDecimal getThreshold(String serviceName) {
        Map<String, ServiceInfoProperties> serviceInfos = monitorProperties.getServiceInfos();
        if (serviceInfos != null && serviceInfos.containsKey(serviceName)) {
            MetricItemAlertProperties<BigDecimal> metricItemAlert = serviceInfos.get(serviceName).getMetric().getTomcat();
            if (metricItemAlert.getThreshold() != null) {
                return metricItemAlert.getThreshold();
            }
        }

        return monitorProperties.getMetric().getTomcat().getThreshold();
    }

    @Override
    public long getCheckIntervalSeconds() {
        return monitorProperties.getTomcatCheckIntervalSeconds();
    }

}