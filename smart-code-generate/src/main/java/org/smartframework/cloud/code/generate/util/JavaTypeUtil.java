package org.smartframework.cloud.code.generate.util;

import java.util.Date;

import org.smartframework.cloud.code.generate.config.Config;

import com.mysql.cj.MysqlType;

import lombok.experimental.UtilityClass;

/**
 * jdbc类型与java类型转化工具类
 * 
 * @author liyulin
 * @date 2019年7月15日 上午10:03:50
 */
@UtilityClass
public class JavaTypeUtil {

	/**
	 * 根据jdbc类型获取对应java类型
	 * 
	 * @param jdbcType
	 * @param length
	 * @return
	 */
	public static String getByJdbcType(int jdbcType, int length) {
		MysqlType mysqlType = MysqlType.getByJdbcType(jdbcType);
		if (length == 1 && (mysqlType == MysqlType.BIT || mysqlType == MysqlType.SMALLINT
				|| mysqlType == MysqlType.SMALLINT_UNSIGNED || mysqlType == MysqlType.INT
				|| mysqlType == MysqlType.INT_UNSIGNED)) {
			return Byte.class.getSimpleName();
		} else if (mysqlType == MysqlType.TIMESTAMP) {
			return Date.class.getSimpleName();
		}
		String className = mysqlType.getClassName();
		int index = className.lastIndexOf('.');
		if (index != -1) {
			return className.substring(index + 1);
		}
		return className;
	}

	/**
	 * 获取要导入的包名
	 * 
	 * @param jdbcType
	 * @return
	 */
	public static String getImportPackage(int jdbcType) {
		MysqlType mysqlType = MysqlType.getByJdbcType(jdbcType);
		if (mysqlType == MysqlType.TIMESTAMP || mysqlType == MysqlType.DATE || mysqlType == MysqlType.DATETIME) {
			return Date.class.getName() + ";";
		}
		return null;
	}

	/**
	 * 获取entity名
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getEntityName(String tableName) {
		return TableUtil.getEntityClassName(tableName) + Config.ENTITY_CLASS_SUFFIX;
	}

	/**
	 * 获取mapper名
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getMapperName(String tableName) {
		return TableUtil.getEntityClassName(tableName) + Config.MAPPER_CLASS_SUFFIX;
	}

	/**
	 * 获取RespBody名
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getBaseRespBodyName(String tableName) {
		return TableUtil.getEntityClassName(tableName) + Config.BASE_RESPBODY_CLASS_SUFFIX;
	}

}