package org.smartframework.cloud.code.generate.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库表元数据信息
 * 
 * @author liyulin
 * @date 2019-07-15
 */
@Getter
@Setter
public class TableMetaDataDto {
	/** 表名 */
	private String name;
	/** 表备注 */
	private String comment;
}