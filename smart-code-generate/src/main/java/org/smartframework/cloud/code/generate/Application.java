package org.smartframework.cloud.code.generate;

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

import javax.swing.plaf.TableUI;

import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.dto.ColumnMetaDataDto;
import org.smartframework.cloud.code.generate.dto.TableMetaDataDto;
import org.smartframework.cloud.code.generate.dto.template.EntityAttributeDto;
import org.smartframework.cloud.code.generate.dto.template.EntityDto;
import org.smartframework.cloud.code.generate.enums.DefaultColumnEnum;
import org.smartframework.cloud.code.generate.util.JavaTypeUtil;
import org.smartframework.cloud.code.generate.util.TableUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connnection = DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/demo_product?serverTimezone=Asia/Shanghai", "root", "123456");
		Map<String, TableMetaDataDto> tableMetaData = getTablesMetaData(connnection);

		DatabaseMetaData metaData = connnection.getMetaData();
		for (Map.Entry<String, TableMetaDataDto> entry : tableMetaData.entrySet()) {
			List<ColumnMetaDataDto> columnMetaDatas = getTableColumnMetaDatas(metaData, connnection.getCatalog(),
					entry.getKey());
			System.out.println(getEntityDto(entry.getValue(), columnMetaDatas));
		}
	}
	
	public static EntityDto getEntityDto(TableMetaDataDto tableMetaData, List<ColumnMetaDataDto> columnMetaDatas) {
		EntityDto entityDto = new EntityDto();
		entityDto.setPackageName(Config.ENTITY_PACKAGE_SUFFIX+Config.ENTITY_SUFFIX);
		entityDto.setTableName(tableMetaData.getName());
		entityDto.setTableComment(tableMetaData.getComment());
		entityDto.setEntityClassName(JavaTypeUtil.getEntityName(tableMetaData.getName()));
		
		List<EntityAttributeDto> attributes = new ArrayList<>();
		entityDto.setAttributes(attributes);
		for(ColumnMetaDataDto columnMetaData:columnMetaDatas) {
			EntityAttributeDto entityAttribute = new EntityAttributeDto();
			entityAttribute.setComment(columnMetaData.getComment());
			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));
			
			attributes.add(entityAttribute);
		}
		return entityDto;
	}

	public static Map<String, TableMetaDataDto> getTablesMetaData(Connection connnection) throws SQLException {
		PreparedStatement preparedStatement = connnection.prepareStatement(
				"SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ?");
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