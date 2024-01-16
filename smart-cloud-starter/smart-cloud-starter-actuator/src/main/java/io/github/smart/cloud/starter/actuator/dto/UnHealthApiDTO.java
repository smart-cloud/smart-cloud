package io.github.smart.cloud.starter.actuator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 不健康接口信息
 *
 * @author collin
 * @date 2024-01-6
 */
@Getter
@Setter
@ToString
public class UnHealthApiDTO {

    /**
     * 接口名
     */
    private String name;
    /**
     * 请求总数
     */
    private Integer total;
    /**
     * 失败数
     */
    private Integer failCount;
    /**
     * 失败率
     */
    private String failRate;

}