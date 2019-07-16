package org.smartframework.cloud.code.generate.dto.template;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntityDto extends CommonDto {

	/** 类注释信息 */
	private ClassCommentDto classComment;
	/** 需要导入的包名 */
	private Set<String> importPackages;
	/** 表名 */
	private String tableName;
	/** 表备注 */
	private String tableComment;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeDto> attributes;

}