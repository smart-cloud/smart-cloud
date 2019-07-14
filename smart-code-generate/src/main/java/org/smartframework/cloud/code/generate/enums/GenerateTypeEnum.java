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
	/** 指定表生成 */
	PART("2");
	
	private String type;

}