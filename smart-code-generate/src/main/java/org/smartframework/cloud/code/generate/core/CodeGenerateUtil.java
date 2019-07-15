package org.smartframework.cloud.code.generate.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.smartframework.cloud.code.generate.config.ConfigCode;
import org.smartframework.cloud.code.generate.dto.ColumnMetaDataDto;
import org.smartframework.cloud.code.generate.dto.TableMetaDataDto;
import org.smartframework.cloud.code.generate.dto.template.BaseMapperDto;
import org.smartframework.cloud.code.generate.dto.template.BaseRespBodyDto;
import org.smartframework.cloud.code.generate.dto.template.CommonDto;
import org.smartframework.cloud.code.generate.dto.template.EntityDto;
import org.smartframework.cloud.code.generate.util.CodeFileGenerateUtil;
import org.smartframework.cloud.code.generate.util.DbUtil;
import org.smartframework.cloud.code.generate.util.PropertiesUtil;
import org.smartframework.cloud.code.generate.util.TemplateDtoUtil;

import lombok.experimental.UtilityClass;

/**
 * 代码生成工具类
 *
 * @author liyulin
 * @date 2019-07-15
 */
@UtilityClass
public class CodeGenerateUtil {

	/**
	 * 生成代码
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void init() throws ClassNotFoundException, SQLException, IOException {
		ResourceBundle resource = PropertiesUtil.getPropertiesBundle();

		try (Connection connnection = DbUtil.getConnection(resource);) {
			Map<String, TableMetaDataDto> tableMetaDataMap = DbUtil.getTablesMetaData(connnection, resource);
			CommonDto commonDto = TemplateDtoUtil.getCommonDto(resource.getString(ConfigCode.Generate.AUTHOR));

			DatabaseMetaData metaData = connnection.getMetaData();
			for (Map.Entry<String, TableMetaDataDto> entry : tableMetaDataMap.entrySet()) {
				generateSingleTable(connnection.getCatalog(), entry.getValue(), metaData, commonDto, resource);
			}
		}
	}

	/**
	 * 生成当个表的代码
	 * 
	 * @param database      数据库名
	 * @param tableMetaData
	 * @param metaData
	 * @param commonDto     公共信息
	 * @param resource      配置信息
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void generateSingleTable(String database, TableMetaDataDto tableMetaData, DatabaseMetaData metaData,
			CommonDto commonDto, ResourceBundle resource) throws SQLException, IOException {
		List<ColumnMetaDataDto> columnMetaDatas = DbUtil.getTableColumnMetaDatas(metaData, database,
				tableMetaData.getName());
		String mainClassPackage = resource.getString(ConfigCode.Generate.MAIN_CLASS_PACKAGE);
		String rpcPath = resource.getString(ConfigCode.Generate.RPC_PATH);
		String servicePath = resource.getString(ConfigCode.Generate.SERVICE_PATH);

		EntityDto entityDto = TemplateDtoUtil.getEntityDto(tableMetaData, columnMetaDatas, commonDto, mainClassPackage);
		CodeFileGenerateUtil.generateEntity(entityDto, servicePath);

		BaseRespBodyDto baseRespBodyDto = TemplateDtoUtil.getBaseRespBodyDto(tableMetaData, columnMetaDatas,
				mainClassPackage, entityDto.getImportPackages());
		CodeFileGenerateUtil.generateBaseRespBody(baseRespBodyDto, rpcPath);

		BaseMapperDto baseMapperDto = TemplateDtoUtil.getBaseMapperDto(tableMetaData, entityDto, baseRespBodyDto,
				commonDto, mainClassPackage);
		CodeFileGenerateUtil.generateBaseMapper(baseMapperDto, servicePath);
	}

}