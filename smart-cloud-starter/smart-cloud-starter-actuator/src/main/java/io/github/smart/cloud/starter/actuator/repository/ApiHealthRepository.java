package io.github.smart.cloud.starter.actuator.repository;

import io.github.smart.cloud.starter.actuator.dto.ApiHealthCacheDTO;
import io.github.smart.cloud.starter.actuator.dto.UnHealthApiDTO;
import io.github.smart.cloud.starter.actuator.properties.HealthProperties;
import io.github.smart.cloud.starter.actuator.util.PercentUtil;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
public class ApiHealthRepository implements SmartInitializingSingleton, DisposableBean {

    private final HealthProperties healthProperties;
    /**
     * 接口成功失败记录统计
     */
    private ConcurrentMap<String, ApiHealthCacheDTO> apiStatusStatistics = new ConcurrentHashMap<>();
    private CreateApiHealthCacheDtoFunction createApiHealthCacheDtoFunction = new CreateApiHealthCacheDtoFunction();
    private ScheduledExecutorService cleanSchedule = null;

    /**
     * 添加接口访问记录
     *
     * @param name
     * @param success
     */
    public void add(String name, boolean success) {
        try {
            ApiHealthCacheDTO apiHealthCacheDTO = apiStatusStatistics.computeIfAbsent(name, createApiHealthCacheDtoFunction);
            if (success) {
                apiHealthCacheDTO.getSuccessCount().increment();
            } else {
                apiHealthCacheDTO.getFailCount().increment();
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
        List<UnHealthApiDTO> unHealthInfos = new ArrayList<>(0);
        apiStatusStatistics.forEach((name, apiStatus) -> {
            BigDecimal failCount = BigDecimal.valueOf(apiStatus.getFailCount().sum());
            BigDecimal total = BigDecimal.valueOf(apiStatus.getSuccessCount().sum()).add(failCount);
            BigDecimal failRate = failCount.divide(total, 4, RoundingMode.HALF_UP);
            if (isUnHealth(name, total, failRate)) {
                UnHealthApiDTO unHealthApiDTO = new UnHealthApiDTO();
                unHealthApiDTO.setName(name);
                unHealthApiDTO.setTotal(total.longValue());
                unHealthApiDTO.setFailCount(failCount.longValue());
                unHealthApiDTO.setFailRate(PercentUtil.format(failRate));
                unHealthInfos.add(unHealthApiDTO);
            }
        });

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
    private final boolean isUnHealth(String name, BigDecimal total, BigDecimal failRate) {
        BigDecimal failRateThreshold = healthProperties.getFailRateThresholds().getOrDefault(name, healthProperties.getDefaultFailRateThreshold());
        return (total.intValue() >= healthProperties.getUnhealthMinCount()) && (failRate.compareTo(failRateThreshold) >= 0);
    }

    @Override
    public void afterSingletonsInstantiated() {
        cleanSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("clean-api-health-cache"));
        cleanSchedule.scheduleWithFixedDelay(() -> apiStatusStatistics.clear(),
                healthProperties.getCleanIntervalSeconds(), healthProperties.getCleanIntervalSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        if (cleanSchedule != null) {
            cleanSchedule.shutdown();
        }
    }

    /**
     * 创建ApiHealthCacheDTO
     *
     * @author collin
     * @date 2024-01-7
     */
    static class CreateApiHealthCacheDtoFunction implements Function<String, ApiHealthCacheDTO> {

        @Override
        public ApiHealthCacheDTO apply(String s) {
            return new ApiHealthCacheDTO(new LongAdder(), new LongAdder());
        }

    }

}