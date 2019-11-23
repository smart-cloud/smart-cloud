package org.smartframework.cloud.code.generate.bo.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 公共信息
 *
 * @author liyulin
 * @date 2019-07-13
 */
@Getter
@Setter
public class CommonBO {

	/** 包名 */
	private String packageName;
	/** 需要导入的包名 */
	private List<String> importPackages;
	/** 类名 */
	private String className;
	/** 表备注 */
	private String tableComment;

}