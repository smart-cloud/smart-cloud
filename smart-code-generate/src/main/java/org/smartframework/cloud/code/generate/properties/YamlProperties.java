package org.smartframework.cloud.code.generate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @desc 代码生成yaml文件配置
 * @author liyulin
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
// yaml文件属性提示需要标注ConfigurationProperties
@ConfigurationProperties
public class YamlProperties {
	/** 数据库连接信息 */
	private DbProperties db;
	/** 待生成的代码信息 */
	private CodeProperties code;
}