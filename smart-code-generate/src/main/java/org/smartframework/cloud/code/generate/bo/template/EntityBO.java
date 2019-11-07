package org.smartframework.cloud.code.generate.bo.template;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntityBO extends CommonBO {

	/** 类注释信息 */
	private ClassCommentBO classComment;
	/** 需要导入的包名 */
	private Set<String> importPackages;
	/** 表名 */
	private String tableName;
	/** 表备注 */
	private String tableComment;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeBO> attributes;

}