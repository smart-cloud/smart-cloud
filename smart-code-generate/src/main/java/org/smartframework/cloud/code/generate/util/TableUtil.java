package org.smartframework.cloud.code.generate.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TableUtil {

	private static final String TABLE_PREFIX = "t_";
	private static final String COLUMN_PREFIX = "f_";
	private static final char SEPARATOR = '_';

	public static String getEntityClassName(String tableName) {
		return getJavaName(tableName, TABLE_PREFIX, true);
	}

	public static String getAttibuteName(String column) {
		return getJavaName(column, COLUMN_PREFIX, false);
	}

	private static String getJavaName(String name, String prefix, boolean isTable) {
		if (name.startsWith(prefix)) {
			name = name.substring(prefix.length());
			if (isTable) {
				String tableNameStart = String.valueOf(name.charAt(0)).toUpperCase();
				name = (name.length() == 1) ? tableNameStart : (tableNameStart + name.substring(1, name.length()));
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

	public static void main(String[] agrs) {
		System.out.println(getEntityClassName("tproduct_info"));
	}

}