package org.smartframework.cloud.code.generate.config;

import lombok.experimental.UtilityClass;

/**
 * 配置code常量
 *
 * @author liyulin
 * @date 2019-07-14
 */
@UtilityClass
public class ConfigCode {

	/** 数据库相关code */
	public static final class Db {
		/** 数据库连接url */
		public static final String URL = "db.url";
		/** 用户名 */
		public static final String USERNAME = "db.username";
		/** 密码 */
		public static final String PASSWORD = "db.password";
	}

	public static final class Generate {
		/** 代码生成人 */
		public static final String AUTHOR = "generate.author";
		/** 生成类型：1、数据库整个表全部生成；2、指定表生成 */
		public static final String TYPE = "generate.type";
		/** 指定要生成的表，多个表用英文逗号（,）隔开 */
		public static final String TABLES = "generate.tables";
		/** 多个表用隔开的分隔符 */
		public static final String TABLES_SEPARATOR = ",";
	}

}