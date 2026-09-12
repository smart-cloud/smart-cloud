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

import io.github.smart.cloud.exception.AbstractBaseException;
import io.github.smart.cloud.starter.monitor.api.core.IApiMonitorDataProcessor;
import io.github.smart.cloud.starter.monitor.api.dto.ApiExceptionAlertDTO;
import io.github.smart.cloud.starter.monitor.api.dto.ApiRequestSummaryDTO;
import io.github.smart.cloud.starter.monitor.api.enums.ApiExceptionRemindType;
import io.github.smart.cloud.starter.monitor.api.enums.DefaultRemindExceptionType;
import io.github.smart.cloud.starter.monitor.api.event.ApiMonitorAlertEvent;
import io.github.smart.cloud.starter.monitor.api.event.ApiMonitorEvent;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.starter.monitor.api.properties.ExceptionApiMonitorProperties;
import io.github.smart.cloud.starter.monitor.api.util.PercentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 异常接口监控处理处理器
 *
 * @author collin
 * @date 2024-01-6
 */
@Slf4j
@RequiredArgsConstructor
public class ExceptionApiMonitorDataProcessor implements IApiMonitorDataProcessor<ApiExceptionAlertDTO>, InitializingBean {

    private final ApiMonitorProperties apiMonitorProperties;
    private final ApiMonitorCacheManager apiMonitorCacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 添加接口访问记录
     *
     * @param event
     */
    @Override
    public void process(ApiMonitorEvent event) {
        try {
            ExceptionApiMonitorProperties exceptionApiMonitorProperties = apiMonitorProperties.getExceptionApiMonitor();
            if (exceptionApiMonitorProperties.getApiWhiteList().contains(event.getApiName())) {
                return;
            }

            ApiRequestSummaryDTO apiRequestSummaryDTO = apiMonitorCacheManager.getApiRequestSummaryDTO(event.getApiName());
            if (event.getThrowable() == null) {
                processSuccessRequest(apiRequestSummaryDTO);
            } else {
                processFailureRequest(event, apiRequestSummaryDTO, exceptionApiMonitorProperties);
            }
        } catch (Throwable e) {
            log.error("api health info add error|name={}", event.getApiName(), e);
        }
    }

    /**
     * 处理成功请求，重置连续失败计数
     *
     * @param apiRequestSummaryDTO
     */
    private void processSuccessRequest(ApiRequestSummaryDTO apiRequestSummaryDTO) {
        apiRequestSummaryDTO.setConsecutiveFailCount(0);
        apiRequestSummaryDTO.setConsecutiveFailAlerted(false);
    }

    /**
     * 处理失败请求，累加计数并检测是否需要告警
     *
     * @param event
     * @param apiRequestSummaryDTO
     * @param exceptionApiMonitorProperties
     */
    private void processFailureRequest(ApiMonitorEvent event, ApiRequestSummaryDTO apiRequestSummaryDTO,
                                       ExceptionApiMonitorProperties exceptionApiMonitorProperties) {
        apiRequestSummaryDTO.setFailCount(apiRequestSummaryDTO.getFailCount() + 1);

        int consecutiveFailCount = apiRequestSummaryDTO.getConsecutiveFailCount() + 1;
        apiRequestSummaryDTO.setConsecutiveFailCount(consecutiveFailCount);

        if (apiRequestSummaryDTO.getErrorAlerted()) {
            return;
        }

        updateErrorSummary(apiRequestSummaryDTO, event);

        // 连续失败熔断告警优先于特殊异常告警
        if (checkConsecutiveFailureAlert(event, apiRequestSummaryDTO, exceptionApiMonitorProperties, consecutiveFailCount)) {
            return;
        }
        checkSpecialExceptionAlert(event, apiRequestSummaryDTO, exceptionApiMonitorProperties);
    }

    /**
     * 更新异常摘要信息（异常对象、traceId、spanId）
     *
     * @param apiRequestSummaryDTO
     * @param event
     */
    private void updateErrorSummary(ApiRequestSummaryDTO apiRequestSummaryDTO, ApiMonitorEvent event) {
        apiRequestSummaryDTO.setThrowable(event.getThrowable());
        if (event.getTraceId() != null) {
            apiRequestSummaryDTO.setErrorTraceId(event.getTraceId());
        }
        if (event.getSpanId() != null) {
            apiRequestSummaryDTO.setErrorSpanId(event.getSpanId());
        }
    }

    /**
     * 检测连续失败熔断并触发告警
     *
     * @param event
     * @param apiRequestSummaryDTO
     * @param exceptionApiMonitorProperties
     * @param consecutiveFailCount
     * @return 是否触发了告警
     */
    private boolean checkConsecutiveFailureAlert(ApiMonitorEvent event, ApiRequestSummaryDTO apiRequestSummaryDTO,
                                                 ExceptionApiMonitorProperties exceptionApiMonitorProperties,
                                                 int consecutiveFailCount) {
        if (apiRequestSummaryDTO.getConsecutiveFailAlerted()) {
            return false;
        }

        int threshold = exceptionApiMonitorProperties.getConsecutiveFailThreshold(event.getApiName());
        if (consecutiveFailCount < threshold) {
            return false;
        }

        apiRequestSummaryDTO.setConsecutiveFailAlerted(true);
        apiRequestSummaryDTO.setErrorAlerted(true);

        ApiExceptionAlertDTO alertDTO = buildBaseAlertDTO(event);
        alertDTO.setConsecutiveFailCount(consecutiveFailCount);
        alertDTO.setRemindType(ApiExceptionRemindType.CONSECUTIVE_FAILURE);
        alertDTO.setNeedAtSomeone(true);
        applicationEventPublisher.publishEvent(ApiMonitorAlertEvent.buildImmediateEvent(this, alertDTO));
        return true;
    }

    /**
     * 检测特殊异常并触发立即告警
     *
     * @param event
     * @param apiRequestSummaryDTO
     * @param exceptionApiMonitorProperties
     */
    private void checkSpecialExceptionAlert(ApiMonitorEvent event, ApiRequestSummaryDTO apiRequestSummaryDTO,
                                            ExceptionApiMonitorProperties exceptionApiMonitorProperties) {
        if (!exceptionApiMonitorProperties.getAlertExceptionMarked()) {
            return;
        }

        if (isSpecialException(event.getThrowable())) {
            apiRequestSummaryDTO.setErrorAlerted(true);
            applicationEventPublisher.publishEvent(ApiMonitorAlertEvent.buildImmediateEvent(this, buildBaseAlertDTO(event)));
        }
    }

    /**
     * 构建基础立即告警DTO（接口名、异常、traceId、spanId）
     *
     * @param event
     * @return
     */
    private ApiExceptionAlertDTO buildBaseAlertDTO(ApiMonitorEvent event) {
        ApiExceptionAlertDTO alertDTO = new ApiExceptionAlertDTO();
        alertDTO.setName(event.getApiName());
        alertDTO.setThrowable(event.getThrowable());
        alertDTO.setTraceId(event.getTraceId());
        alertDTO.setSpanId(event.getSpanId());
        return alertDTO;
    }

    /**
     * 是否是特殊异常
     *
     * @param throwable
     * @return
     */
    private boolean isSpecialException(Throwable throwable) {
        ExceptionApiMonitorProperties exceptionApiMonitorProperties = apiMonitorProperties.getExceptionApiMonitor();
        Set<String> needAlertExceptionClassNames = exceptionApiMonitorProperties.getNeedAlertExceptionClassNames();
        if (needAlertExceptionClassNames.contains(throwable.getClass().getSimpleName())) {
            return true;
        }

        Set<String> needAlertExceptionCodes = exceptionApiMonitorProperties.getNeedAlertExceptionCodes();
        if (!CollectionUtils.isEmpty(needAlertExceptionCodes)) {
            String exceptionCode = ExceptionCodeProcessor.getExceptionCode(throwable);
            if (exceptionCode != null && needAlertExceptionCodes.contains(exceptionCode)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 查询不健康的接口信息
     *
     * @return
     */
    @Override
    public List<ApiExceptionAlertDTO> getAlertRecords() {
        Map<String, ApiRequestSummaryDTO> apiRequestSummaryCache = apiMonitorCacheManager.getApiRequestSummaryCache();
        if (apiRequestSummaryCache.isEmpty()) {
            return Collections.emptyList();
        }

        List<ApiExceptionAlertDTO> apiExceptions = new ArrayList<>(0);
        for (Map.Entry<String, ApiRequestSummaryDTO> entry : apiRequestSummaryCache.entrySet()) {
            String name = entry.getKey();
            ApiRequestSummaryDTO apiRequestSummary = entry.getValue();
            long failCountCount = apiRequestSummary.getFailCount();
            if (failCountCount == 0) {
                continue;
            }

            BigDecimal failCount = BigDecimal.valueOf(failCountCount);
            long totalCountValue = apiRequestSummary.getTotalCount();
            if (totalCountValue <= 0) {
                continue;
            }
            BigDecimal total = BigDecimal.valueOf(totalCountValue);
            BigDecimal failRate = failCount.divide(total, 4, RoundingMode.HALF_UP);
            ApiExceptionRemindType remindType = match(name, total, failRate, apiRequestSummary.getThrowable());
            if (remindType != ApiExceptionRemindType.NONE) {
                ApiExceptionAlertDTO apiExceptionAlertDTO = new ApiExceptionAlertDTO();
                apiExceptionAlertDTO.setName(name);
                apiExceptionAlertDTO.setTotalCount(total.longValue());
                apiExceptionAlertDTO.setFailCount(failCount.longValue());
                apiExceptionAlertDTO.setFailRate(PercentUtil.format(failRate));
                apiExceptionAlertDTO.setThrowable(apiRequestSummary.getThrowable());
                apiExceptionAlertDTO.setTraceId(apiRequestSummary.getErrorTraceId());
                apiExceptionAlertDTO.setSpanId(apiRequestSummary.getErrorSpanId());
                apiExceptionAlertDTO.setRemindType(remindType);
                apiExceptionAlertDTO.setNeedAtSomeone(ApiExceptionRemindType.FAIL_RATE == remindType);
                apiExceptionAlertDTO.setAlerted(apiRequestSummary.getErrorAlerted());
                apiExceptions.add(apiExceptionAlertDTO);
            }
        }

        if (!apiExceptions.isEmpty()) {
            ExceptionApiMonitorProperties exceptionApiMonitorProperties = apiMonitorProperties.getExceptionApiMonitor();
            Collections.sort(apiExceptions, (o1, o2) -> {
                ApiExceptionRemindType remindType1 = o1.getRemindType();
                ApiExceptionRemindType remindType2 = o2.getRemindType();
                // 异常信息类型排在前
                if (ApiExceptionRemindType.EXCEPTION_INFO == remindType1 && ApiExceptionRemindType.EXCEPTION_INFO != remindType2) {
                    return -1;
                }
                if (ApiExceptionRemindType.EXCEPTION_INFO != remindType1 && ApiExceptionRemindType.EXCEPTION_INFO == remindType2) {
                    return 1;
                }

                // 按失败率倒叙排序，使用 BigDecimal 避免 long 乘法溢出。
                BigDecimal left = BigDecimal.valueOf(o2.getFailCount()).multiply(BigDecimal.valueOf(o1.getTotalCount()));
                BigDecimal right = BigDecimal.valueOf(o1.getFailCount()).multiply(BigDecimal.valueOf(o2.getTotalCount()));
                return left.compareTo(right);
            });
            // 异常接口超过最大上报数量时，进行裁剪
            if (apiExceptions.size() > exceptionApiMonitorProperties.getApiReportMaxCount()) {
                return apiExceptions.subList(0, exceptionApiMonitorProperties.getApiReportMaxCount());
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
     * @param throwable
     * @return
     */
    private ApiExceptionRemindType match(String name, BigDecimal total, BigDecimal failRate, Throwable throwable) {
        ExceptionApiMonitorProperties exceptionApiMonitorProperties = apiMonitorProperties.getExceptionApiMonitor();
        if (exceptionApiMonitorProperties.getAlertExceptionMarked()) {
            if (isSpecialException(throwable)) {
                return ApiExceptionRemindType.EXCEPTION_INFO;
            }
        }

        BigDecimal failRateThreshold = exceptionApiMonitorProperties.getFailRateThresholds().getOrDefault(name, exceptionApiMonitorProperties.getDefaultFailRateThreshold());
        if (total.intValue() >= exceptionApiMonitorProperties.getMatchMinCount() && failRate.compareTo(failRateThreshold) >= 0) {
            return ApiExceptionRemindType.FAIL_RATE;
        }
        return ApiExceptionRemindType.NONE;
    }

    @Override
    public void afterPropertiesSet() {
        ExceptionApiMonitorProperties exceptionApiMonitorProperties = apiMonitorProperties.getExceptionApiMonitor();
        Set<String> needAlertExceptionClassNames = exceptionApiMonitorProperties.getNeedAlertExceptionClassNames();
        needAlertExceptionClassNames.addAll(buildDefaultNeedAlertExceptionClassNames());
    }

    /**
     * 构建默认需要@提醒的异常类型
     *
     * @return
     */
    private Set<String> buildDefaultNeedAlertExceptionClassNames() {
        Set<String> defaultNeedAlertExceptionClassNames = new HashSet<>(32);
        DefaultRemindExceptionType[] defaultRemindExceptionTypes = DefaultRemindExceptionType.values();
        for (DefaultRemindExceptionType remindExceptionType : defaultRemindExceptionTypes) {
            defaultNeedAlertExceptionClassNames.add(remindExceptionType.getName());
        }
        return defaultNeedAlertExceptionClassNames;
    }

    private static class ExceptionCodeProcessor {

        /**
         * 缓存getCode方法
         */
        private static final ClassValue<Method> CODE_METHOD_CLASS_VALUE = new ClassValue<Method>() {
            @Override
            protected Method computeValue(Class<?> type) {
                try {
                    // 首次访问时，反射查找getCode方法（无参）
                    return type.getMethod("getCode");
                } catch (NoSuchMethodException e) {
                    return null; // 无此方法，返回null
                }
            }
        };

        /**
         * 从异常类中获取异常code
         *
         * @param throwable
         * @return
         */
        private static String getExceptionCode(Throwable throwable) {
            if (throwable == null) {
                return null;
            }

            if (throwable instanceof AbstractBaseException) {
                AbstractBaseException baseException = (AbstractBaseException) throwable;
                return baseException.getCode();
            }

            // 非AbstractBaseException子类，尝试反射调用getCode方法
            Method method = CODE_METHOD_CLASS_VALUE.get(throwable.getClass());
            if (method == null) {
                return null;
            }

            try {
                // 优化：设置accessible为true，减少安全检查开销
                method.setAccessible(true);
                Object code = method.invoke(throwable);
                if (code instanceof String) {
                    return (String) code;
                }
                return code == null ? null : String.valueOf(code);
            } catch (Exception e) {
                log.error("get exception code fail|throwable={}", throwable, e);
                return null;
            }
        }
    }

}