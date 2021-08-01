package org.smartframework.cloud.starter.configure.constants;

/**
 * 公共常量定义
 *
 * @author liyulin
 * @date 2019-04-22
 */
public interface SmartConstant {

    /**
     * 公共配置属性前缀
     */
    String SMART_PROPERTIES_PREFIX = "smart";
    /**
     * rpc 日志打印开关配置name
     */
    String FEIGN_LOG_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".log.rpclog";
    /**
     * api 日志打印开关配置name
     */
    String API_LOG_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".log.apilog";

}