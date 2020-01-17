package org.smartframework.cloud.code.generate.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库表元数据信息
 * 
 * @author liyulin
 * @date 2019-07-15
 */
@Getter
@Setter
@ToString
public class TableMetaDataBO {
	/** 表名 */
	private String name;
	/** 表备注 */
	private String comment;
}