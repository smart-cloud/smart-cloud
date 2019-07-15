package org.smartframework.cloud.code.generate.dto.template;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntityDto {

	private CommonDto common;
	/** 包名 */
	private String packageName;
	/** 需要导入的包名 */
	private Set<String> importPackages;
	/** 表名 */
	private String tableName;
	/** 表备注 */
	private String tableComment;
	/** 类名 */
	private String className;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeDto> attributes;

}