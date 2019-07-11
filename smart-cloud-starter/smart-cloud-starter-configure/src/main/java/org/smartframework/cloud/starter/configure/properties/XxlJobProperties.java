package org.smartframework.cloud.starter.configure.properties;

import lombok.Data;

@Data
public class XxlJobProperties {

	/** admin端地址 */
	private String adminAddresses;

	/** job应用名 */
	private String appName;

	/** admin端ip */
	private String ip;

	/** admin端端口 */
	private Integer port;

	/** 访问admin端的token */
	private String accessToken;

	/** 日志保存路径 */
	private String logPath;

	/** 日志保存天数（默认30天） */
	private Integer logRetentionDays = 30;

}