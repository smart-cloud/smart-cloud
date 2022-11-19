package io.github.smart.cloud.starter.configure.properties;

import io.github.smart.cloud.common.pojo.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * 异步相关配置
 *
 * @author collin
 * @date 2022-02-06
 */
@Getter
@Setter
public class AsyncProperties extends Base {

    /**
     * 可用处理器数
     */
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    /**
     * 是否可用（默认true）
     */
    private boolean enable = true;

    /**
     * 核心线程数大小（默认为可用处理器数）
     */
    private int corePoolSize = AVAILABLE_PROCESSORS;

    /**
     * 最大线程数大小
     */
    private int maxPoolSize = (AVAILABLE_PROCESSORS << 2) + 1;

    /**
     * 非核心线程存活时间（单位秒，默认60秒）
     */
    private int keepAliveSeconds = 60;

    /**
     * 队列大小（默认512）
     */
    private int queueCapacity = 512;

    /**
     * 执行程序在关闭时应该等待的最大秒数（默认0）
     */
    private int awaitTerminationSeconds = 0;

}