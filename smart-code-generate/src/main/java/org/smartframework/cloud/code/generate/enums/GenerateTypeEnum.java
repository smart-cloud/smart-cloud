package org.smartframework.cloud.code.generate.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表生成类型
 *
 * @author liyulin
 * @date 2019-07-14
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GenerateTypeEnum {

	/** 数据库整个表全部生成 */
	ALL("1"),
	/** 只生成指定的表 */
	INCLUDE("2"),
	/** 除了指定的表，全部生成 */
	EXCLUDE("3");

	private String type;

}