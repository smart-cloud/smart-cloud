package org.smartframework.cloud.code.generate.dto.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntityDto {
	/** 包名 */
	private String packageName;
	/** 表名 */
	private String tableName;
	/** 表备注 */
	private String tableComment;
	/** entity类名 */
	private String entityClassName;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeDto> attributes;
	
}