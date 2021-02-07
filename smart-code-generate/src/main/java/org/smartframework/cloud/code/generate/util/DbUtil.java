package org.smartframework.cloud.code.generate.util;

import com.mysql.cj.MysqlType;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.DbProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作工具类
 *
 * @author liyulin
 * @date 2019-07-13
 */
@UtilityClass
public class DbUtil {

    /**
     * 表字段转义符
     */
    private static final String TABLE_FIELD_ESCAPES = "`";
    /**
     * 表字段备注转义符
     */
    private static final String TABLE_FIELD_COMMENT_ESCAPES = "'";

    /**
     * 获取数据库连接
     *
     * @param db
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(DbProperties db) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
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
        Map<String, TableMetaDataBO> tableMetaDataBOs = new HashMap<>();
        try (PreparedStatement preparedStatement = connnection.prepareStatement(String.format("SHOW TABLE STATUS FROM `%s`", connnection.getCatalog()));
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                String tableName = resultSet.getString("Name");
                if (filterTable(code, tableName)) {
                    continue;
                }
                TableMetaDataBO tableBO = new TableMetaDataBO();
                tableBO.setName(tableName);
                tableBO.setComment(resultSet.getString("Comment"));

                tableMetaDataBOs.put(tableBO.getName(), tableBO);
            }
        }
        return tableMetaDataBOs;
    }

    private boolean filterTable(CodeProperties code, String tableName) {
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
     * @throws JSQLParserException
     */
    public static List<ColumnMetaDataBO> getTableColumnMetaDatas(Connection connnection, String database,
                                                                 String tableName) throws SQLException, JSQLParserException {
        String createTableSql = null;
        try (PreparedStatement preparedStatement = connnection.prepareStatement(String.format("SHOW CREATE TABLE `%s`.`%s`", database, tableName));
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                createTableSql = resultSet.getString(2);
                break;
            }
        }

        CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(createTableSql);
        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();

        List<ColumnMetaDataBO> columnMetaDatas = new ArrayList<>();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            ColumnMetaDataBO columnMetaData = new ColumnMetaDataBO();
            columnMetaData.setName(wrapTableFieldName(columnDefinition.getColumnName()));

            String comment = columnDefinition.getColumnSpecs().get(columnDefinition.getColumnSpecs().size() - 1);
            columnMetaData.setComment(wrapTableFieldComment(comment));

            int jdbcType = MysqlType.getByName(columnDefinition.getColDataType().getDataType()).getJdbcType();
            columnMetaData.setJdbcType(jdbcType);

            List<String> dataTypeArguments = columnDefinition.getColDataType().getArgumentsStringList();
            if (dataTypeArguments != null && dataTypeArguments.size() > 0) {
                columnMetaData.setLength(Integer.valueOf(dataTypeArguments.get(0)));
            }

            columnMetaDatas.add(columnMetaData);
        }

        return columnMetaDatas;
    }

    /**
     * 去除表字段名前后的转义符"`"
     *
     * @param name
     * @return
     */
    private String wrapTableFieldName(String name) {
        if (name.startsWith(TABLE_FIELD_ESCAPES) && name.endsWith(TABLE_FIELD_ESCAPES)) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    private String wrapTableFieldComment(String comment) {
        if (comment.startsWith(TABLE_FIELD_COMMENT_ESCAPES) && comment.endsWith(TABLE_FIELD_COMMENT_ESCAPES)) {
            return comment.substring(1, comment.length() - 1);
        }
        if ("NULL".equals(comment)) {
            return null;
        }
        return comment;
    }

}