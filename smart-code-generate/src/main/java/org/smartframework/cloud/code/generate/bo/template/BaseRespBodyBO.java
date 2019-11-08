package org.smartframework.cloud.code.generate.bo.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRespBodyBO extends CommonBO {
	
	/** 需要导入的包名 */
	private List<String> importPackages;
	/** 表备注 */
	private String tableComment;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeBO> attributes;
	
}