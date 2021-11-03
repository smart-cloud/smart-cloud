package org.smartframework.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * entity属性信息
 *
 * @author liyulin
 * @date 2019-07-13
 */
@Getter
@Setter
@ToString
public class EntityAttributeBO {

	/**
	 * 是否为主键
	 */
	private Boolean primaryKey;
	/** 表字段对应的java属性名 */
	private String name;
	/** 表字段名称 */
	private String columnName;
	/** 类型 */
	private String javaType;
	/** 备注 */
	private String comment;
	/** mask规则 */
	private String maskRule;

}