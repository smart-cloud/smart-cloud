package org.smartframework.cloud.code.generate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMetaDataDto {
	/** 表名 */
	private String name;
	/** 表备注 */
	private String comment;
}