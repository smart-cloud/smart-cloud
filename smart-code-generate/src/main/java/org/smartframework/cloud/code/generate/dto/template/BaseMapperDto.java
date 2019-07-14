package org.smartframework.cloud.code.generate.dto.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseMapperDto {
	
	private CommonDto common;
	/** 包名 */
	private String packageName;
	/** 表备注 */
	private String tableComment;
	/** 类名 */
	private String className;
	
	/** entity类名 */
	private String entityClassName;
	/** entity package */
	private String importEntityClass;
	
	/** baseRespBody类名 */
	private String baseRespBodyClassName;
	/** baseRespBody package */
	private String importBaseRespBodyClass;
	
}