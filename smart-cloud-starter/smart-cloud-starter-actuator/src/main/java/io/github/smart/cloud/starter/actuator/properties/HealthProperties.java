package io.github.smart.cloud.starter.actuator.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口健康检测配置属性
 * <p/>
 * <b>配置样例：</b>
 * <pre>
 * health:
 *   unhealthMinCount: 10
 *   defaultFailRateThreshold: 0.3
 *   failRateThresholds:
 *     '[LoginController#login]': 0
 *     '[OrderController#query]': 0
 * </pre>
 *
 * @author collin
 * @date 2024-01-6
 */
@Getter
@Setter
@ToString
public class HealthProperties {

    /**
     * 不健康最小数量
     */
    private int unhealthMinCount = 5;
    /**
     * 默认失败阈值（默认0.5）
     */
    private BigDecimal defaultFailRateThreshold = BigDecimal.valueOf(0.5);
    /**
     * 特定接口失败阈值
     */
    private Map<String, BigDecimal> failRateThresholds = new HashMap<>();
    /**
     * 清理间隔时间（单位：秒）
     */
    private long cleanIntervalSeconds = 60 * 5L;

}