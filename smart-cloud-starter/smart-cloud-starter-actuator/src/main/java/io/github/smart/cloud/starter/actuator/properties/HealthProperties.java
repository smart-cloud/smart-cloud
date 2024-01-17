package io.github.smart.cloud.starter.actuator.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 接口健康检测配置属性
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
     * 失败阈值（默认0.5）
     */
    private BigDecimal failRateThreshold = BigDecimal.valueOf(0.5);
    /**
     * 清理间隔时间（单位：秒）
     */
    private long cleanIntervalSeconds = 60 * 5L;

}