package org.smartframework.cloud.code.generate.dto.template;

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
public class EntityAttributeDto {
	
	/** 表字段对应的java属性名 */
	private String name;
	/** 类型 */
	private String javaType;
	/** 备注 */
	private String comment;
	
}