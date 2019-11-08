package org.smartframework.cloud.code.generate.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.bo.template.BaseMapperBO;
import org.smartframework.cloud.code.generate.bo.template.BaseRespBodyBO;
import org.smartframework.cloud.code.generate.bo.template.ClassCommentBO;
import org.smartframework.cloud.code.generate.bo.template.EntityBO;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.PathProperties;
import org.smartframework.cloud.code.generate.properties.YamlProperties;
import org.smartframework.cloud.code.generate.util.CodeFileGenerateUtil;
import org.smartframework.cloud.code.generate.util.DbUtil;
import org.smartframework.cloud.code.generate.util.TemplateBOUtil;
import org.smartframework.cloud.code.generate.util.YamlPropertiesCheckUtil;
import org.smartframework.cloud.code.generate.util.YamlUtil;

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
		YamlProperties yamlProperties = YamlUtil.readYamlProperties();
		YamlPropertiesCheckUtil.check(yamlProperties);
		
		CodeProperties codeProperties = yamlProperties.getCode();
		try (Connection connnection = DbUtil.getConnection(yamlProperties.getDb());) {
			Map<String, TableMetaDataBO> tableMetaDataMap = DbUtil.getTablesMetaData(connnection, codeProperties);
			ClassCommentBO classComment = TemplateBOUtil.getClassCommentBO(codeProperties.getAuthor());

			DatabaseMetaData metaData = connnection.getMetaData();
			for (Map.Entry<String, TableMetaDataBO> entry : tableMetaDataMap.entrySet()) {
				generateSingleTable(connnection.getCatalog(), entry.getValue(), metaData, classComment, codeProperties);
			}
		}
	}

	/**
	 * 生成当个表的代码
	 * 
	 * @param database      数据库名
	 * @param tableMetaData
	 * @param metaData
	 * @param classComment  公共信息
	 * @param code
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void generateSingleTable(String database, TableMetaDataBO tableMetaData, DatabaseMetaData metaData,
			ClassCommentBO classComment, CodeProperties code) throws SQLException, IOException {
		List<ColumnMetaDataBO> columnMetaDatas = DbUtil.getTableColumnMetaDatas(metaData, database,
				tableMetaData.getName());
		String mainClassPackage = code.getMainClassPackage();
		PathProperties pathProperties = code.getProject().getPath();
		String rpcPath = pathProperties.getRpc();
		String servicePath = pathProperties.getService();

		EntityBO entityDto = TemplateBOUtil.getEntityBO(tableMetaData, columnMetaDatas, classComment, mainClassPackage);
		CodeFileGenerateUtil.generateEntity(entityDto, servicePath);

		BaseRespBodyBO baseRespBodyDto = TemplateBOUtil.getBaseRespBodyBO(tableMetaData, columnMetaDatas,
				mainClassPackage, entityDto.getImportPackages());
		CodeFileGenerateUtil.generateBaseRespBody(baseRespBodyDto, rpcPath);

		BaseMapperBO baseMapperDto = TemplateBOUtil.getBaseMapperBO(tableMetaData, entityDto, baseRespBodyDto,
				classComment, mainClassPackage);
		CodeFileGenerateUtil.generateBaseMapper(baseMapperDto, servicePath);
	}

}