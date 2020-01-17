package org.smartframework.cloud.code.generate.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.enums.DefaultColumnEnum;
import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.DbProperties;

import lombok.experimental.UtilityClass;

/**
 * 数据库操作工具类
 *
 * @author liyulin
 * @date 2019-07-13
 */
@UtilityClass
public class DbUtil {

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
		String qryTableMetaDataSql = getQueryTableMetaDataSql(connnection.getCatalog(), code.getType(),
				code.getSpecifiedTables());

		Map<String, TableMetaDataBO> tableMetaDataBOs = new HashMap<>();
		try (PreparedStatement preparedStatement = connnection.prepareStatement(qryTableMetaDataSql);
				ResultSet resultSet = preparedStatement.executeQuery();) {
			while (resultSet.next()) {
				TableMetaDataBO tableBO = new TableMetaDataBO();
				tableBO.setName(resultSet.getString(1));
				tableBO.setComment(resultSet.getString(2));

				tableMetaDataBOs.put(tableBO.getName(), tableBO);
			}
		}

		return tableMetaDataBOs;
	}

	/**
	 * 获取查询表信息的sql
	 * 
	 * @param dbName
	 * @param generateType
	 * @param tableNameStr
	 * @return
	 */
	private static String getQueryTableMetaDataSql(String dbName, Integer generateType, String tableNameStr) {
		StringBuilder qryTableMetaDataSql = new StringBuilder();
		qryTableMetaDataSql
				.append("SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '");
		qryTableMetaDataSql.append(dbName);
		qryTableMetaDataSql.append("' ");
		if (GenerateTypeEnum.ALL.getType().compareTo(generateType) != 0) {
			String[] tableNames = tableNameStr.split(Config.TABLES_SEPARATOR);
			qryTableMetaDataSql.append("AND table_name ");
			if (GenerateTypeEnum.EXCLUDE.getType().compareTo(generateType) == 0) {
				qryTableMetaDataSql.append("NOT ");
			}
			qryTableMetaDataSql.append("IN(");
			for (int i = 0; i < tableNames.length; i++) {
				qryTableMetaDataSql.append("'");
				qryTableMetaDataSql.append(tableNames[i]);
				qryTableMetaDataSql.append("'");
				if (i < tableNames.length - 1) {
					qryTableMetaDataSql.append(",");
				}
			}
			qryTableMetaDataSql.append(")");
		}
		return qryTableMetaDataSql.toString();
	}

	/**
	 * 获取表字段信息
	 * 
	 * @param metaData
	 * @param database
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static List<ColumnMetaDataBO> getTableColumnMetaDatas(DatabaseMetaData metaData, String database,
			String tableName) throws SQLException {
		List<ColumnMetaDataBO> columnMetaDatas = new ArrayList<>();
		try (ResultSet resultSet = metaData.getColumns(database, "", tableName, null);) {
			while (resultSet.next()) {
				String name = resultSet.getString("COLUMN_NAME");
				if (DefaultColumnEnum.contains(name)) {
					continue;
				}
				ColumnMetaDataBO columnMetaData = new ColumnMetaDataBO();
				columnMetaData.setName(name);
				columnMetaData.setComment(resultSet.getString("REMARKS"));
				columnMetaData.setJdbcType(resultSet.getInt("DATA_TYPE"));
				columnMetaData.setLength(resultSet.getInt("COLUMN_SIZE"));

				columnMetaDatas.add(columnMetaData);
			}
		}

		return columnMetaDatas;
	}

}