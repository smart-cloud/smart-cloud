package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @desc 数据库连接配置
 * @author liyulin
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class DbProperties {
	/** 数据库连接url */
	private String url;
	/** 数据库用户名 */
	private String username;
	/** 用户库密码 */
	private String password;
}