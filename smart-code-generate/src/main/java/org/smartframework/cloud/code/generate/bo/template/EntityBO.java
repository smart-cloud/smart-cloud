package org.smartframework.cloud.code.generate.bo.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntityBO extends CommonBO {

	/** 类注释信息 */
	private ClassCommentBO classComment;
	/** 表名 */
	private String tableName;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeBO> attributes;

}