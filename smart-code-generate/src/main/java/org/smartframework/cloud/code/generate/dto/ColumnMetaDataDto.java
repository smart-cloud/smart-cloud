package org.smartframework.cloud.code.generate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnMetaDataDto {

	/** 表字段名 */
	private String name;
	/** 数据类型 */
	private int jdbcType;
	/** 字段备注 */
	private String comment;
	/** 字段长度 */
	private int length;

}