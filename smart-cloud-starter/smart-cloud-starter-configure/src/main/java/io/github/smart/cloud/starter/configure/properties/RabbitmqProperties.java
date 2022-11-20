package io.github.smart.cloud.starter.configure.properties;

import io.github.smart.cloud.common.pojo.Base;
import io.github.smart.cloud.constants.LogLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * rabbitmq配置属性
 *
 * @author collin
 * @date 2022-11-20
 */
@Getter
@Setter
public class RabbitmqProperties extends Base {

    /**
     * 日志级别（默认DEBUG）
     *
     * @see LogLevel
     */
    private String level = LogLevel.DEBUG;

}