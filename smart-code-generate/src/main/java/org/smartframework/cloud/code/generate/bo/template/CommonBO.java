package org.smartframework.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 公共信息
 *
 * @author liyulin
 * @date 2019-07-13
 */
@Getter
@Setter
@ToString
public class CommonBO {

	/** 包名 */
	private String packageName;
	/** 需要导入的包名 */
	private Set<String> importPackages;
	/** 类名 */
	private String className;
	/** 表备注 */
	private String tableComment;
	/** 类注释信息 */
	private ClassCommentBO classComment;

}