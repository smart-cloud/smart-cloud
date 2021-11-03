package org.smartframework.cloud.starter.log.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扩展属性
 *
 * @author collin
 * @date 2021-10-30
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExtProperty {

    /**
     * 应用名
     */
    APP_NAME("appName"),
    /**
     * 日志保存根路径
     */
    LOG_PATH("logPath");

    private String name;

}