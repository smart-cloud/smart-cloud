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
package io.github.smart.cloud.starter.monitor.actuator.repository;

import io.github.smart.cloud.starter.monitor.actuator.dto.ApiHealthCacheDTO;
import io.github.smart.cloud.starter.monitor.actuator.dto.UnHealthApiDTO;
import io.github.smart.cloud.starter.monitor.actuator.properties.HealthProperties;
import io.github.smart.cloud.starter.monitor.actuator.util.PercentUtil;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class ApiHealthRepository implements InitializingBean, DisposableBean {

    private final HealthProperties healthProperties;
    /**
     * 接口成功失败记录统计
     */
    private ConcurrentMap<String, ApiHealthCacheDTO> apiStatusStatistics = new ConcurrentHashMap<>();
    private CreateApiHealthCacheDtoFunction createApiHealthCacheDtoFunction = new CreateApiHealthCacheDtoFunction();
    private ScheduledExecutorService cleanSchedule;

    /**
     * 添加接口访问记录
     *
     * @param name
     * @param success
     * @param exception
     */
    public void add(String name, boolean success, Exception exception) {
        try {
            if (healthProperties.getApiWhiteList().contains(name)) {
                return;
            }

            ApiHealthCacheDTO apiHealthCacheDTO = apiStatusStatistics.computeIfAbsent(name, createApiHealthCacheDtoFunction);
            if (success) {
                apiHealthCacheDTO.getSuccessCount().increment();
            } else {
                apiHealthCacheDTO.getFailCount().increment();
                apiHealthCacheDTO.setExceptionMessage(exception.toString());
            }
        } catch (Exception e) {
            log.error("api health info add error|name={}", name, e);
        }
    }

    /**
     * 查询不健康的接口信息
     *
     * @return
     */
    public List<UnHealthApiDTO> getUnHealthInfos() {
        final List<UnHealthApiDTO> unHealthInfos = new ArrayList<>(0);
        apiStatusStatistics.forEach((name, apiHealthCacheDTO) -> {
            BigDecimal failCount = BigDecimal.valueOf(apiHealthCacheDTO.getFailCount().sum());
            BigDecimal total = BigDecimal.valueOf(apiHealthCacheDTO.getSuccessCount().sum()).add(failCount);
            BigDecimal failRate = failCount.divide(total, 4, RoundingMode.HALF_UP);
            if (isUnHealth(name, total, failRate)) {
                UnHealthApiDTO unHealthApiDTO = new UnHealthApiDTO();
                unHealthApiDTO.setName(name);
                unHealthApiDTO.setTotal(total.longValue());
                unHealthApiDTO.setFailCount(failCount.longValue());
                unHealthApiDTO.setFailRate(PercentUtil.format(failRate));
                unHealthApiDTO.setFailMessage(apiHealthCacheDTO.getExceptionMessage());
                unHealthInfos.add(unHealthApiDTO);
            }
        });

        if (CollectionUtils.isNotEmpty(unHealthInfos)) {
            if (unHealthInfos.size() > healthProperties.getUnhealthApiReportMaxCount()) {
                Collections.sort(unHealthInfos, (o1, o2) -> {
                    // 按失败率倒叙排序
                    return (int) (o2.getFailCount() * o1.getTotal() - o1.getFailCount() * o2.getTotal());
                });
                return unHealthInfos.subList(0, healthProperties.getUnhealthApiReportMaxCount());
            }
        }
        return unHealthInfos;
    }

    /**
     * 判断是否不健康
     *
     * @param total
     * @param name
     * @param failRate
     * @return
     */
    private boolean isUnHealth(String name, BigDecimal total, BigDecimal failRate) {
        BigDecimal failRateThreshold = healthProperties.getFailRateThresholds().getOrDefault(name, healthProperties.getDefaultFailRateThreshold());
        return (total.intValue() >= healthProperties.getUnhealthMatchMinCount()) && (failRate.compareTo(failRateThreshold) >= 0);
    }

    @Override
    public void afterPropertiesSet() {
        cleanSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("clean-api-health-cache"));
        cleanSchedule.scheduleWithFixedDelay(this::clearApiStatusStatistics, healthProperties.getCleanIntervalSeconds(),
                healthProperties.getCleanIntervalSeconds(), TimeUnit.SECONDS);
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