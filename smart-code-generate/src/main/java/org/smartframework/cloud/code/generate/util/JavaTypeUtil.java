package org.smartframework.cloud.code.generate.util;

import org.smartframework.cloud.code.generate.config.Config;

import com.mysql.cj.MysqlType;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JavaTypeUtil {

	public static String getByJdbcType(int jdbcType, int length) {
		MysqlType mysqlType = MysqlType.getByJdbcType(jdbcType);
		if (length == 1 && (mysqlType == MysqlType.SMALLINT || mysqlType == MysqlType.SMALLINT_UNSIGNED
				|| mysqlType == MysqlType.INT || mysqlType == MysqlType.INT_UNSIGNED)) {
			return Byte.class.getSimpleName();
		}
		String className = mysqlType.getClassName();
		int index = className.lastIndexOf('.');
		if (index != -1) {
			return className.substring(index + 1);
		}
		return className;
	}

	public static String getEntityName(String tableName) {
		return TableUtil.getEntityClassName(tableName) + Config.ENTITY_CLASS_SUFFIX;
	}
	
	public static String getMapperName(String tableName) {
		return TableUtil.getEntityClassName(tableName) + Config.MAPPER_CLASS_SUFFIX;
	}
	
	public static String getBaseRespBodyName(String tableName) {
		return TableUtil.getEntityClassName(tableName) + Config.BASE_RESPBODY_CLASS_SUFFIX;
	}

}