package org.smartframework.cloud.code.generate.bo.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseMapperBO extends CommonBO {

	/** 类注释信息 */
	private ClassCommentBO classComment;
	/** 需要导入的包名 */
	private List<String> importPackages;
	
	/** entity类名 */
	private String entityClassName;
	/** baseRespBody类名 */
	private String baseRespBodyClassName;
	
}