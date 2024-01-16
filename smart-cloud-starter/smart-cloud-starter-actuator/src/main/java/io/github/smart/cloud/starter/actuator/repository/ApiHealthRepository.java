package io.github.smart.cloud.starter.actuator.repository;

import io.github.smart.cloud.starter.actuator.dto.ApiHealthCacheDTO;
import io.github.smart.cloud.starter.actuator.dto.UnHealthApiDTO;
import io.github.smart.cloud.starter.actuator.properties.HealthProperties;
import io.github.smart.cloud.starter.actuator.util.LruCache;
import io.github.smart.cloud.starter.actuator.util.PercentUtil;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    private LruCache<String, ApiHealthCacheDTO> apiStatusStatistics = null;
    private ScheduledExecutorService cleanSchedule = null;
    private final Object MONITOR = new Object();

    /**
     * 添加接口访问记录
     *
     * @param name
     * @param success
     */
    public void add(String name, boolean success) {
        try {
            ApiHealthCacheDTO apiHealthCacheDTO = apiStatusStatistics.get(name);
            if (apiHealthCacheDTO == null) {
                synchronized (MONITOR) {
                    apiHealthCacheDTO = apiStatusStatistics.get(name);
                    if (apiHealthCacheDTO == null) {
                        apiHealthCacheDTO = new ApiHealthCacheDTO(new AtomicInteger(0), new AtomicInteger(0));
                        apiStatusStatistics.put(name, apiHealthCacheDTO);
                    }
                }
            }

            if (success) {
                apiHealthCacheDTO.getSuccessCount().incrementAndGet();
            } else {
                apiHealthCacheDTO.getFailCount().incrementAndGet();
            }
        } catch (Exception e) {
            log.error("web api health info add error|name={}", name, e);
        }
    }

    /**
     * 查询不健康的接口信息
     *
     * @return
     */
    public List<UnHealthApiDTO> getUnHealthInfos() {
        List<UnHealthApiDTO> unHealthInfos = new ArrayList<>(0);
        synchronized (MONITOR) {
            apiStatusStatistics.forEach((name, apiStatus) -> {
                BigDecimal failCount = BigDecimal.valueOf(apiStatus.getFailCount().get());
                BigDecimal total = BigDecimal.valueOf(apiStatus.getSuccessCount().get()).add(failCount);
                BigDecimal failRate = failCount.divide(total, 4, RoundingMode.HALF_UP);
                if (isUnHealth(total, failRate)) {
                    UnHealthApiDTO unHealthApiDTO = new UnHealthApiDTO();
                    unHealthApiDTO.setName(name);
                    unHealthApiDTO.setTotal(total.intValue());
                    unHealthApiDTO.setFailCount(failCount.intValue());
                    unHealthApiDTO.setFailRate(PercentUtil.format(failRate));
                    unHealthInfos.add(unHealthApiDTO);
                }
            });
        }

        return unHealthInfos;
    }

    /**
     * 判断是否不健康
     *
     * @param total
     * @param failRate
     * @return
     */
    private final boolean isUnHealth(BigDecimal total, BigDecimal failRate) {
        return (total.intValue() >= healthProperties.getUnhealthMinCount()) && (failRate.compareTo(healthProperties.getFailRateThreshold()) > 0);
    }

    @Override
    public void afterSingletonsInstantiated() {
        Assert.isTrue(healthProperties.getCacheSize() > 0, "cacheSize must be greater than 0");
        apiStatusStatistics = new LruCache<>(healthProperties.getCacheSize());

        cleanSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("clean-api-health-cache"));
        cleanSchedule.scheduleWithFixedDelay(() -> {
            synchronized (MONITOR) {
                apiStatusStatistics.clear();
            }
        }, healthProperties.getCleanIntervalSeconds(), healthProperties.getCleanIntervalSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        if (cleanSchedule != null) {
            cleanSchedule.shutdown();
        }
    }

}