package org.smartframework.cloud.starter.configure.properties;

import org.smartframework.cloud.common.pojo.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XxlJobProperties extends BaseDto {

	private static final long serialVersionUID = 1L;

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