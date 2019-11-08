package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @desc 工程路径配置
 * @author liyulin
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class PathProperties {
	/** rpc工程路径 */
	private String rpc;
	/** 服务工程路径 */
	private String service;
}