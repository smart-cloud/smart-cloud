/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.code.generate.util;

import com.mysql.cj.MysqlType;

import java.util.Date;

/**
 * jdbc类型与java类型转化工具类
 *
 * @author collin
 * @date 2019年7月15日 上午10:03:50
 */
public class JavaTypeUtil {

    private JavaTypeUtil() {
    }

    /**
     * 根据jdbc类型获取对应java类型
     *
     * @param jdbcType
     * @param length
     * @return
     */
    public static String getByJdbcType(int jdbcType, int length) {
        MysqlType mysqlType = MysqlType.getByJdbcType(jdbcType);
        if (isByteType(mysqlType, length)) {
            return Byte.class.getSimpleName();
        } else if (mysqlType == MysqlType.TIMESTAMP || mysqlType == MysqlType.DATE || mysqlType == MysqlType.DATETIME) {
            return Date.class.getSimpleName();
        } else if (mysqlType == MysqlType.BLOB || mysqlType == MysqlType.VARBINARY
                || mysqlType == MysqlType.TINYBLOB || mysqlType == MysqlType.MEDIUMBLOB
                || mysqlType == MysqlType.LONGBLOB || mysqlType == MysqlType.BINARY
                || mysqlType == MysqlType.GEOMETRY) {
            return byte[].class.getSimpleName();
        } else if (mysqlType == MysqlType.UNKNOWN) {
            return Object.class.getSimpleName();
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
            return Date.class.getName();
        }
        return null;
    }

    private static boolean isByteType(MysqlType mysqlType, int length) {
        return (length <= 3 && (mysqlType == MysqlType.TINYINT || mysqlType == MysqlType.TINYINT_UNSIGNED))
                || (length <= 1 && mysqlType == MysqlType.BIT);

}

}