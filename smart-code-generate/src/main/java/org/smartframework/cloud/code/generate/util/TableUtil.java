package org.smartframework.cloud.code.generate.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TableUtil {

	/** 表前缀 */
	private static final String[] TABLE_PREFIX = { "t_" };
	/** 字段前缀 */
	private static final String[] COLUMN_PREFIXS = { "f_", "t_" };
	/** 表、字段名称分隔符 */
	private static final char SEPARATOR = '_';

	public static String getEntityClassName(String tableName) {
		return getJavaName(tableName, TABLE_PREFIX, true);
	}

	public static String getAttibuteName(String column) {
		return getJavaName(column, COLUMN_PREFIXS, false);
	}

	private static String getJavaName(String name, String[] prefixs, boolean isTable) {
		for (String prefix : prefixs) {
			if (name.startsWith(prefix)) {
				name = name.substring(prefix.length());
				if (isTable) {
					String tableNameStart = String.valueOf(name.charAt(0)).toUpperCase();
					name = (name.length() == 1) ? tableNameStart : (tableNameStart + name.substring(1, name.length()));
				}
			}
		}

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == SEPARATOR) {
				String tableNameStart = name.substring(0, i);
				char tableNameMiddle = name.charAt(i + 1);
				String tableNameEnd = name.substring(i + 2, name.length());
				name = tableNameStart + String.valueOf(tableNameMiddle).toUpperCase() + tableNameEnd;
			}
		}
		return name;
	}

}