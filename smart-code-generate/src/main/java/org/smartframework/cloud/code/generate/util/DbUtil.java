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
import java.util.ResourceBundle;

import org.smartframework.cloud.code.generate.config.ConfigCode;
import org.smartframework.cloud.code.generate.dto.ColumnMetaDataDto;
import org.smartframework.cloud.code.generate.dto.TableMetaDataDto;
import org.smartframework.cloud.code.generate.enums.DefaultColumnEnum;
import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;

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
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(ResourceBundle resource) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(resource.getString(ConfigCode.Db.URL),
				resource.getString(ConfigCode.Db.USERNAME), resource.getString(ConfigCode.Db.PASSWORD));
	}

	/**
	 * 获取表信息
	 * 
	 * @param connnection
	 * @param resource
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, TableMetaDataDto> getTablesMetaData(Connection connnection, ResourceBundle resource)
			throws SQLException {
		String qryTableMetaDataSql = getQueryTableMetaDataSql(connnection.getCatalog(),
				resource.getString(ConfigCode.Generate.TYPE), resource.getString(ConfigCode.Generate.TABLES));

		Map<String, TableMetaDataDto> tableMetaDataDtos = new HashMap<>();
		try (PreparedStatement preparedStatement = connnection.prepareStatement(qryTableMetaDataSql);
				ResultSet resultSet = preparedStatement.executeQuery();) {
			while (resultSet.next()) {
				TableMetaDataDto tableDto = new TableMetaDataDto();
				tableDto.setName(resultSet.getString(1));
				tableDto.setComment(resultSet.getString(2));

				tableMetaDataDtos.put(tableDto.getName(), tableDto);
			}
		}

		return tableMetaDataDtos;
	}

	/**
	 * 获取查询表信息的sql
	 * 
	 * @param dbName
	 * @param generateType
	 * @param tableNameStr
	 * @return
	 */
	private static String getQueryTableMetaDataSql(String dbName, String generateType, String tableNameStr) {
		StringBuilder qryTableMetaDataSql = new StringBuilder();
		qryTableMetaDataSql
				.append("SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '");
		qryTableMetaDataSql.append(dbName);
		qryTableMetaDataSql.append("' ");
		if (!GenerateTypeEnum.ALL.getType().equals(generateType)) {
			String[] tableNames = tableNameStr.split(ConfigCode.Generate.TABLES_SEPARATOR);
			qryTableMetaDataSql.append("AND table_name ");
			if (GenerateTypeEnum.EXCLUDE.getType().equals(generateType)) {
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
	public static List<ColumnMetaDataDto> getTableColumnMetaDatas(DatabaseMetaData metaData, String database,
			String tableName) throws SQLException {
		List<ColumnMetaDataDto> columnMetaDatas = new ArrayList<>();
		try (ResultSet resultSet = metaData.getColumns(database, "", tableName, null);) {
			while (resultSet.next()) {
				String name = resultSet.getString("COLUMN_NAME");
				if (DefaultColumnEnum.contains(name)) {
					continue;
				}
				ColumnMetaDataDto columnMetaData = new ColumnMetaDataDto();
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