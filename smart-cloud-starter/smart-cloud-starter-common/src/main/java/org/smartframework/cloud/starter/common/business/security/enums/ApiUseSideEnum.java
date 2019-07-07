package org.smartframework.cloud.starter.common.business.security.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口使用端
 * 
 * @author liyulin
 * @date 2019年7月3日 下午2:30:45
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiUseSideEnum {

	/** app、h5端，且非后台管理端使用 */
	API("/api/"),
	/** 后台管理端使用 */
	OMS("/oms/"),
	/** 服务之间使用 */
	RPC("/rpc/");

	/** url片段 */
	private String pathSegment;

}