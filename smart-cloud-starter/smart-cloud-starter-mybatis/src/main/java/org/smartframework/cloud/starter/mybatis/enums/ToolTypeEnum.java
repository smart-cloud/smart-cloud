package org.smartframework.cloud.starter.mybatis.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 集成的工具类型
 * 
 * @author liyulin
 * @date 2019-07-17
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ToolTypeEnum {

	/** 通用mapper */
	MAPPER("mapper"),
	/** mybatis plust */
	MYBATIS_PLUS("mybatisplus");

	private String type;

}