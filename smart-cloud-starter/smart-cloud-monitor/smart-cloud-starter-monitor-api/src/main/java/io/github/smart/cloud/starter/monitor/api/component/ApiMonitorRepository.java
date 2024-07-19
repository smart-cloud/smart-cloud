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
package io.github.smart.cloud.starter.monitor.api.component;

import io.github.smart.cloud.starter.monitor.api.dto.ApiExceptionDTO;
import io.github.smart.cloud.starter.monitor.api.dto.ApiHealthCacheDTO;
import io.github.smart.cloud.starter.monitor.api.enums.ApiExceptionRemindType;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.starter.monitor.api.util.PercentUtil;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

/**
 * 接口健康信息存储
 *
 * @author collin
 * @date 2024-01-6
 */
@Slf4j
@RequiredArgsConstructor
public class ApiMonitorRepository implements InitializingBean, DisposableBean, ApplicationListener<RefreshScopeRefreshedEvent> {

    private final ApiMonitorProperties apiMonitorProperties;
    /**
     * 接口成功失败记录统计
     */
    private ConcurrentMap<String, ApiHealthCacheDTO> apiStatusStatistics = new ConcurrentHashMap<>();
    private CreateApiHealthCacheDtoFunction createApiHealthCacheDtoFunction = new CreateApiHealthCacheDtoFunction();
    private ScheduledExecutorService cleanSchedule;
    private Set<String> needAlertExceptionClassNames;

    /**
     * 添加接口访问记录
     *
     * @param name
     * @param success
     * @param throwable
     */
    public void add(String name, boolean success, Throwable throwable) {
        try {
            if (apiMonitorProperties.getApiWhiteList().contains(name)) {
                return;
            }

            ApiHealthCacheDTO apiHealthCacheDTO = apiStatusStatistics.computeIfAbsent(name, createApiHealthCacheDtoFunction);
            if (success) {
                apiHealthCacheDTO.getSuccessCount().increment();
            } else {
                apiHealthCacheDTO.getFailCount().increment();
                if (throwable != null) {
                    apiHealthCacheDTO.setMessage(throwable.toString());
                }
            }
        } catch (Throwable e) {
            log.error("api health info add error|name={}", name, e);
        }
    }

    /**
     * 查询不健康的接口信息
     *
     * @return
     */
    public List<ApiExceptionDTO> getApiExceptions() {
        if (apiStatusStatistics.isEmpty()) {
            return Collections.emptyList();
        }

        List<ApiExceptionDTO> apiExceptions = new ArrayList<>(0);
        for (Map.Entry<String, ApiHealthCacheDTO> entry : apiStatusStatistics.entrySet()) {
            String name = entry.getKey();
            ApiHealthCacheDTO apiHealthCacheDTO = entry.getValue();
            long failCountSum = apiHealthCacheDTO.getFailCount().sum();
            if (failCountSum == 0) {
                continue;
            }

            BigDecimal failCount = BigDecimal.valueOf(failCountSum);
            BigDecimal total = BigDecimal.valueOf(apiHealthCacheDTO.getSuccessCount().sum()).add(failCount);
            BigDecimal failRate = failCount.divide(total, 4, RoundingMode.HALF_UP);
            ApiExceptionRemindType remindType = match(name, total, failRate, apiHealthCacheDTO.getMessage());
            if (remindType != ApiExceptionRemindType.NONE) {
                ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO();
                apiExceptionDTO.setName(name);
                apiExceptionDTO.setTotal(total.longValue());
                apiExceptionDTO.setFailCount(failCount.longValue());
                apiExceptionDTO.setFailRate(PercentUtil.format(failRate));
                apiExceptionDTO.setMessage(apiHealthCacheDTO.getMessage());
                apiExceptionDTO.setRemindType(remindType);
                apiExceptions.add(apiExceptionDTO);
            }
        }

        if (!apiExceptions.isEmpty()) {
            if (apiExceptions.size() > apiMonitorProperties.getUnhealthApiReportMaxCount()) {
                Collections.sort(apiExceptions, (o1, o2) -> {
                    // 按失败率倒叙排序
                    return (int) (o2.getFailCount() * o1.getTotal() - o1.getFailCount() * o2.getTotal());
                });
                return apiExceptions.subList(0, apiMonitorProperties.getUnhealthApiReportMaxCount());
            }
        }
        return apiExceptions;
    }

    /**
     * 判断是否不健康
     *
     * @param total
     * @param name
     * @param failRate
     * @param message
     * @return
     */
    private ApiExceptionRemindType match(String name, BigDecimal total, BigDecimal failRate, String message) {
        if (apiMonitorProperties.getAlertExceptionMarked()) {
            if (!CollectionUtils.isEmpty(needAlertExceptionClassNames)) {
                for (String needAlertExceptionClassName : needAlertExceptionClassNames) {
                    if (message.contains(needAlertExceptionClassName)) {
                        return ApiExceptionRemindType.EXCEPTION_INFO;
                    }
                }
            }
        }

        BigDecimal failRateThreshold = apiMonitorProperties.getFailRateThresholds().getOrDefault(name, apiMonitorProperties.getDefaultFailRateThreshold());
        if (total.intValue() >= apiMonitorProperties.getUnhealthMatchMinCount() && failRate.compareTo(failRateThreshold) >= 0) {
            return ApiExceptionRemindType.FAIL_RATE;

        }
        return ApiExceptionRemindType.NONE;
    }

    @Override
    public void afterPropertiesSet() {
        needAlertExceptionClassNames = apiMonitorProperties.getNeedAlertExceptionClassNames();
        if (CollectionUtils.isEmpty(needAlertExceptionClassNames)) {
            Set<String> defaultNeedAlertExceptionClassNames = new HashSet<>(8);
            defaultNeedAlertExceptionClassNames.add(SQLException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(ConcurrentModificationException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(NullPointerException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(IndexOutOfBoundsException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(ArrayIndexOutOfBoundsException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(ClassCastException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(StackOverflowError.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(OutOfMemoryError.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(IllegalMonitorStateException.class.getSimpleName());
            defaultNeedAlertExceptionClassNames.add(StringIndexOutOfBoundsException.class.getSimpleName());

            needAlertExceptionClassNames = defaultNeedAlertExceptionClassNames;
        }

        cleanSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("clean-api-health-cache"));
        cleanSchedule.scheduleWithFixedDelay(this::clearApiStatusStatistics, apiMonitorProperties.getCleanIntervalSeconds(),
                apiMonitorProperties.getCleanIntervalSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        if (cleanSchedule != null) {
            cleanSchedule.shutdown();
        }

        clearApiStatusStatistics();
    }

    public void clearApiStatusStatistics() {
        apiStatusStatistics.clear();
    }

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        // 处理“@RefreshScope会导致ScheduledExecutorService失效”的问题
    }

    /**
     * 创建ApiHealthCacheDTO
     *
     * @author collin
     * @date 2024-01-7
     */
    private class CreateApiHealthCacheDtoFunction implements Function<String, ApiHealthCacheDTO> {

        @Override
        public ApiHealthCacheDTO apply(String s) {
            return new ApiHealthCacheDTO(new LongAdder(), new LongAdder(), null);
        }

    }

}