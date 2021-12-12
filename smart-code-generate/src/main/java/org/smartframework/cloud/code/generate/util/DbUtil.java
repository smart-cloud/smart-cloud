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
package org.smartframework.cloud.code.generate.util;

import com.mysql.cj.MysqlType;
import org.apache.commons.io.FileUtils;
import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.constants.DbConstants;
import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.DbProperties;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

/**
 * 数据库操作工具类
 *
 * @author collin
 * @date 2019-07-13
 */
public class DbUtil {

    private DbUtil() {
    }

    /**
     * 获取数据库连接
     *
     * @param db
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(DbProperties db) throws ClassNotFoundException, SQLException, IOException {
        Class.forName(db.getDriverClassName());

        Properties props = new Properties();
        props.setProperty(DbConstants.ConnectionProperties.USER, db.getUsername());
        props.setProperty(DbConstants.ConnectionProperties.PASSWORD, db.getPassword());
        // 获取Oracle元数据 REMARKS信息
        props.setProperty(DbConstants.ConnectionProperties.REMARKS_REPORTING, "true");
        // 获取MySQL元数据 REMARKS信息
        props.setProperty(DbConstants.ConnectionProperties.USE_INFORMATION_SCHEMA, "true");
        Connection connection = DriverManager.getConnection(db.getUrl(), props);
        String schema = db.getSchema();
        if (schema != null && schema.trim().length() > 0) {
            PreparedStatement preparedStatement = null;
            try {
                String fileContent = FileUtils.readFileToString(new ClassPathResource(schema).getFile(), StandardCharsets.UTF_8);
                if (fileContent != null && fileContent.trim().length() > 0) {
                    preparedStatement = connection.prepareStatement(fileContent.trim());
                    preparedStatement.execute();
                }
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        }
        return connection;
    }

    /**
     * 获取表信息
     *
     * @param connnection
     * @param code
     * @return
     * @throws SQLException
     */
    public static Map<String, TableMetaDataBO> getTablesMetaData(Connection connnection, CodeProperties code)
            throws SQLException {
        Map<String, TableMetaDataBO> tableMetaDataMap = new HashMap<>(16);
        try (ResultSet resultSet = connnection.getMetaData().getTables(connnection.getCatalog(), null, null, new String[]{DbConstants.TABLE_TYPE})) {
            while (resultSet.next()) {
                String tableName = resultSet.getString(3);
                if (filterTable(code, tableName)) {
                    continue;
                }
                TableMetaDataBO tableBO = new TableMetaDataBO();
                tableBO.setName(tableName);
                tableBO.setComment(resultSet.getString(5));

                tableMetaDataMap.put(tableBO.getName(), tableBO);
            }
        }
        return tableMetaDataMap;
    }

    private static boolean filterTable(CodeProperties code, String tableName) {
        if (GenerateTypeEnum.ALL.getType().compareTo(code.getType()) == 0) {
            return false;
        }

        if (GenerateTypeEnum.INCLUDE.getType().compareTo(code.getType()) == 0) {
            String[] specifiedTables = code.getSpecifiedTables().split(Config.TABLES_SEPARATOR);
            for (String specifiedTable : specifiedTables) {
                if (specifiedTable.equals(tableName)) {
                    return false;
                }
            }
            return true;
        }

        if (GenerateTypeEnum.EXCLUDE.getType().compareTo(code.getType()) == 0) {
            String[] specifiedTables = code.getSpecifiedTables().split(Config.TABLES_SEPARATOR);
            for (String specifiedTable : specifiedTables) {
                if (specifiedTable.equals(tableName)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    /**
     * 获取表字段信息
     *
     * @param connnection
     * @param database
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static List<ColumnMetaDataBO> getTableColumnMetaDatas(Connection connnection, String database,
                                                                 String tableName) throws SQLException {
        DatabaseMetaData metaData = connnection.getMetaData();
        List<ColumnMetaDataBO> columnMetaDatas = new ArrayList<>();
        try (ResultSet columnsResultSet = metaData.getColumns(database, null, tableName, null)) {
            while (columnsResultSet.next()) {
                ColumnMetaDataBO columnMetaData = new ColumnMetaDataBO();
                columnMetaData.setName(columnsResultSet.getString(4));
                columnMetaData.setComment(columnsResultSet.getString(12));
                columnMetaData.setJdbcType(MysqlType.getByName(columnsResultSet.getString(6)).getJdbcType());

                columnMetaData.setLength(columnsResultSet.getInt(7));
                columnMetaData.setPrimaryKey(false);
                columnMetaDatas.add(columnMetaData);
            }
        }

        // 主键
        String primaryKeyColumnName = getPrimaryKeyColumnName(metaData, database, tableName);
        if (primaryKeyColumnName != null) {
            columnMetaDatas.stream().filter(columnMetaData -> columnMetaData.getName().equals(primaryKeyColumnName))
                    .findFirst()
                    .ifPresent(columnMetaData -> {
                        columnMetaData.setPrimaryKey(true);
                    });
        }

        return columnMetaDatas;
    }

    /**
     * 获取主键字段名
     *
     * @param metaData
     * @param database
     * @param tableName
     * @return 值为null则表示没有主键
     * @throws SQLException
     */
    private static String getPrimaryKeyColumnName(DatabaseMetaData metaData, String database,
                                                  String tableName) throws SQLException {
        try (ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(database, null, tableName)) {
            while (primaryKeyResultSet.next()) {
                return primaryKeyResultSet.getString(4);
            }
        }
        return null;
    }

}