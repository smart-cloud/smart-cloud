package io.github.smart.cloud.starter.actuator.dto;

import lombok.*;

import java.util.concurrent.atomic.LongAdder;

/**
 * 接口访问状态（成功、失败）缓存信息
 *
 * @author collin
 * @date 2024-01-6
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiHealthCacheDTO {

    /**
     * 成功数
     */
    private volatile LongAdder successCount;
    /**
     * 失败数
     */
    private volatile LongAdder failCount;

}