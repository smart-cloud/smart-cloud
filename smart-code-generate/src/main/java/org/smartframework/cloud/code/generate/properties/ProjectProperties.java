package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @desc 工程配置
 * @author liyulin
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class ProjectProperties {
	/** 工程路径配置 */
	private PathProperties path;
}