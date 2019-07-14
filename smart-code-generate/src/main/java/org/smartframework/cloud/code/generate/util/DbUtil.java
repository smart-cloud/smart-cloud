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
		Connection connnection = DriverManager.getConnection(resource.getString(ConfigCode.Db.URL),
				resource.getString(ConfigCode.Db.USERNAME), resource.getString(ConfigCode.Db.PASSWORD));
		return connnection;
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
		StringBuilder qryTableMetaDataSql = new StringBuilder();
		qryTableMetaDataSql
				.append("SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ?");
		if (GenerateTypeEnum.PART.getType().equals(resource.getString(ConfigCode.Generate.TYPE))) {
			String tableNameStr = resource.getString(ConfigCode.Generate.TABLES);
			String[] tableNames = tableNameStr.split(ConfigCode.Generate.TABLES_SEPARATOR);
			qryTableMetaDataSql.append(" AND table_name IN(");
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
		PreparedStatement preparedStatement = connnection.prepareStatement(qryTableMetaDataSql.toString());
		preparedStatement.setString(1, connnection.getCatalog());

		ResultSet resultSet = preparedStatement.executeQuery();
		Map<String, TableMetaDataDto> tableMetaDataDtos = new HashMap<>();
		while (resultSet.next()) {
			TableMetaDataDto tableDto = new TableMetaDataDto();
			tableDto.setName(resultSet.getString(1));
			tableDto.setComment(resultSet.getString(2));

			tableMetaDataDtos.put(tableDto.getName(), tableDto);
		}
		return tableMetaDataDtos;
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
		ResultSet resultSet = metaData.getColumns(database, "", tableName, null);
		List<ColumnMetaDataDto> columnMetaDatas = new ArrayList<>();
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
		return columnMetaDatas;
	}

}