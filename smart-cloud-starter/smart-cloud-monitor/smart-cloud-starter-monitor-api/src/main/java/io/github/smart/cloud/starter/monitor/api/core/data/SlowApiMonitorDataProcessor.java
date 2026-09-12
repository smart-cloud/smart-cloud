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
package io.github.smart.cloud.starter.monitor.api.core.data;

import io.github.smart.cloud.starter.monitor.api.core.IApiMonitorDataProcessor;
import io.github.smart.cloud.starter.monitor.api.dto.ApiRequestSummaryDTO;
import io.github.smart.cloud.starter.monitor.api.dto.ApiSlowAlertDTO;
import io.github.smart.cloud.starter.monitor.api.event.ApiMonitorAlertEvent;
import io.github.smart.cloud.starter.monitor.api.event.ApiMonitorEvent;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.starter.monitor.api.properties.SlowApiMonitorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 慢接口监控数据处理器
 *
 * @author collin.li
 * @datge 2025-09-19
 */
@Slf4j
@RequiredArgsConstructor
public class SlowApiMonitorDataProcessor implements IApiMonitorDataProcessor<ApiSlowAlertDTO> {

    private final ApiMonitorProperties apiMonitorProperties;
    private final ApiMonitorCacheManager apiMonitorCacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void process(ApiMonitorEvent event) {
        try {
            SlowApiMonitorProperties slowApiMonitorProperties = apiMonitorProperties.getSlowApiMonitor();
            String apiName = event.getApiName();
            if (slowApiMonitorProperties.getApiWhiteList().contains(apiName)) {
                return;
            }
            long cost = event.getCost();
            long costThreshold = event.getCostThreshold() > 0 ? event.getCostThreshold() : slowApiMonitorProperties.getCostThreshold(apiName);
            if (cost < costThreshold) {
                return;
            }

            ApiRequestSummaryDTO apiRequestSummary = apiMonitorCacheManager.getApiRequestSummaryDTO(apiName);
            apiRequestSummary.setSlowCount(apiRequestSummary.getSlowCount() + 1);
            long maxCost = apiRequestSummary.getMaxCost();
            if (cost > maxCost) {
                apiRequestSummary.setMaxCost(cost);
                if (event.getTraceId() != null) {
                    apiRequestSummary.setSlowTraceId(event.getTraceId());
                }
                if (event.getSpanId() != null) {
                    apiRequestSummary.setSlowSpanId(event.getSpanId());
                }
            }

            if (apiRequestSummary.getSlowAlerted()) {
                return;
            }

            if (cost >= slowApiMonitorProperties.getAtSomeoneCostThresholds(apiName)) {
                apiRequestSummary.setSlowAlerted(true);

                // 超过@提醒值，立即发告警
                ApiSlowAlertDTO apiSlowAlert = new ApiSlowAlertDTO();
                apiSlowAlert.setMaxCost(cost);
                apiSlowAlert.setNeedAtSomeone(true);
                apiSlowAlert.setName(apiName);
                apiSlowAlert.setTraceId(event.getTraceId());
                apiSlowAlert.setSpanId(event.getSpanId());
                applicationEventPublisher.publishEvent(ApiMonitorAlertEvent.buildImmediateEvent(this, apiSlowAlert));
            }
        } catch (Throwable e) {
            log.error("api slow info add error|name={}", event.getApiName(), e);
        }
    }

    @Override
    public List<ApiSlowAlertDTO> getAlertRecords() {
        Map<String, ApiRequestSummaryDTO> apiRequestSummaryCache = apiMonitorCacheManager.getApiRequestSummaryCache();
        if (apiRequestSummaryCache.isEmpty()) {
            return Collections.emptyList();
        }

        SlowApiMonitorProperties slowApiMonitorProperties = apiMonitorProperties.getSlowApiMonitor();
        List<ApiSlowAlertDTO> apiSlowAlerts = new ArrayList<>(0);
        for (Map.Entry<String, ApiRequestSummaryDTO> entry : apiRequestSummaryCache.entrySet()) {
            String apiName = entry.getKey();
            ApiRequestSummaryDTO apiRequestSummary = entry.getValue();
            long slowCount = apiRequestSummary.getSlowCount();
            if (slowCount == 0) {
                continue;
            }

            if (apiRequestSummary.getMaxCost() >= slowApiMonitorProperties.getAtSomeoneCostThresholds(apiName)) {
                ApiSlowAlertDTO apiSlowAlert = new ApiSlowAlertDTO();
                apiSlowAlert.setName(apiName);
                apiSlowAlert.setSlowCount(apiRequestSummary.getSlowCount());
                apiSlowAlert.setTotalCount(apiRequestSummary.getTotalCount());
                apiSlowAlert.setMaxCost(apiRequestSummary.getMaxCost());
                apiSlowAlert.setTraceId(apiRequestSummary.getSlowTraceId());
                apiSlowAlert.setSpanId(apiRequestSummary.getSlowSpanId());
                apiSlowAlert.setNeedAtSomeone(false);
                apiSlowAlert.setAlerted(apiRequestSummary.getSlowAlerted());

                apiSlowAlerts.add(apiSlowAlert);
                continue;
            }


            long totalCount = apiRequestSummary.getTotalCount();
            if (totalCount <= 0) {
                continue;
            }
            BigDecimal slowRateThreshold = slowApiMonitorProperties.getSlowRateThreshold(apiName);
            BigDecimal slowRate = BigDecimal.valueOf(slowCount).divide(BigDecimal.valueOf(totalCount), 4, RoundingMode.HALF_UP);

            ApiSlowAlertDTO apiSlowAlert = new ApiSlowAlertDTO();
            apiSlowAlert.setName(apiName);
            apiSlowAlert.setSlowCount(slowCount);
            apiSlowAlert.setTotalCount(totalCount);
            apiSlowAlert.setMaxCost(apiRequestSummary.getMaxCost());
            apiSlowAlert.setSlowRate(slowRate);
            apiSlowAlert.setTraceId(apiRequestSummary.getSlowTraceId());
            apiSlowAlert.setSpanId(apiRequestSummary.getSlowSpanId());
            apiSlowAlert.setNeedAtSomeone(slowRate.compareTo(slowRateThreshold) >= 0);
            apiSlowAlert.setAlerted(apiRequestSummary.getSlowAlerted());

            apiSlowAlerts.add(apiSlowAlert);
        }

        if (!apiSlowAlerts.isEmpty()) {
            if (apiSlowAlerts.size() > slowApiMonitorProperties.getApiReportMaxCount()) {
                /**
                 * slowRate为null的排前面，不为null的排后面
                 * slowRate都为null时，按maxCost降序
                 * slowRate都不为null时，按slowRate降序
                 */
                Collections.sort(apiSlowAlerts, (a, b) -> {
                    if (a.getSlowRate() == null && b.getSlowRate() != null) {
                        return -1;
                    }
                    if (a.getSlowRate() != null && b.getSlowRate() == null) {
                        return 1;
                    }
                    if (a.getSlowRate() == null && b.getSlowRate() == null) {
                        return Long.compare(b.getMaxCost(), a.getMaxCost());
                    }
                    return b.getSlowRate().compareTo(a.getSlowRate());
                });
                // 取前N个
                return apiSlowAlerts.subList(0, slowApiMonitorProperties.getApiReportMaxCount());
            }
        }

        return apiSlowAlerts;
    }

}