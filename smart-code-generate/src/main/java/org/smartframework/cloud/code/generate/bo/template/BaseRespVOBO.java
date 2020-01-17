package org.smartframework.cloud.code.generate.bo.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRespVOBO extends CommonBO {

	/** 是否mask注解 */
	private String enableMask;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeBO> attributes;

}