package org.smartframework.cloud.code.generate.dto.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRespBodyDto {
	
	/** 包名 */
	private String packageName;
	/** 表备注 */
	private String tableComment;
	/** 类名 */
	private String className;
	/** 表字段对应的java属性信息 */
	List<EntityAttributeDto> attributes;
	
}