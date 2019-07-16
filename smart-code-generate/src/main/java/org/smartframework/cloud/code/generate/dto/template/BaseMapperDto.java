package org.smartframework.cloud.code.generate.dto.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseMapperDto extends CommonDto {

	/** 类注释信息 */
	private ClassCommentDto classComment;
	/** 表备注 */
	private String tableComment;
	
	/** entity类名 */
	private String entityClassName;
	/** entity package */
	private String importEntityClass;
	
	/** baseRespBody类名 */
	private String baseRespBodyClassName;
	/** baseRespBody package */
	private String importBaseRespBodyClass;
	
}