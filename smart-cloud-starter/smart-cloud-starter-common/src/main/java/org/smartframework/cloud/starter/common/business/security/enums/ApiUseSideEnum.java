package org.smartframework.cloud.starter.common.business.security.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口使用端
 * 
 * @author liyulin
 * @date 2019-07-03
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