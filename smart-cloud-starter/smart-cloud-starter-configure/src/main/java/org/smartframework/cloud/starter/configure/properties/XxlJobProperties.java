package org.smartframework.cloud.starter.configure.properties;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;

/**
 * xxl-job配置属性
 *
 * @author collin
 * @date 2021-10-31
 */
@Getter
@Setter
public class XxlJobProperties extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * admin端地址
     */
    private String adminAddresses;

    /**
     * job应用名
     */
    private String appName;

    /**
     * admin端ip
     */
    private String ip;

    /**
     * admin端端口
     */
    private Integer port;

    /**
     * 访问admin端的token
     */
    private String accessToken;

    /**
     * 日志保存路径
     */
    private String logPath;

    /**
     * 日志保存天数（默认30天）
     */
    private Integer logRetentionDays = 30;

}