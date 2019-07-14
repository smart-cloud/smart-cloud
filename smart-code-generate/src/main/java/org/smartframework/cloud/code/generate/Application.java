package org.smartframework.cloud.code.generate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.config.ConfigCode;
import org.smartframework.cloud.code.generate.dto.ColumnMetaDataDto;
import org.smartframework.cloud.code.generate.dto.TableMetaDataDto;
import org.smartframework.cloud.code.generate.dto.template.BaseMapperDto;
import org.smartframework.cloud.code.generate.dto.template.BaseRespBodyDto;
import org.smartframework.cloud.code.generate.dto.template.CommonDto;
import org.smartframework.cloud.code.generate.dto.template.EntityAttributeDto;
import org.smartframework.cloud.code.generate.dto.template.EntityDto;
import org.smartframework.cloud.code.generate.util.DbUtil;
import org.smartframework.cloud.code.generate.util.FreeMarkerUtil;
import org.smartframework.cloud.code.generate.util.JavaTypeUtil;
import org.smartframework.cloud.code.generate.util.PropertiesUtil;
import org.smartframework.cloud.code.generate.util.TableUtil;

public class Application {

	public static CommonDto getCommonDto(String author) {
		CommonDto commonDto = new CommonDto();
		commonDto.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		commonDto.setAuthor(author);
		return commonDto;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		ResourceBundle resource = PropertiesUtil.getBundle("config/mall_product");
		Connection connnection = DbUtil.getConnection(resource);
		Map<String, TableMetaDataDto> tableMetaDataMap = DbUtil.getTablesMetaData(connnection, resource);
		CommonDto commonDto = getCommonDto(resource.getString(ConfigCode.Generate.AUTHOR));

		DatabaseMetaData metaData = connnection.getMetaData();
		for (Map.Entry<String, TableMetaDataDto> entry : tableMetaDataMap.entrySet()) {
			TableMetaDataDto tableMetaData = entry.getValue();
			List<ColumnMetaDataDto> columnMetaDatas = DbUtil.getTableColumnMetaDatas(metaData, connnection.getCatalog(),
					entry.getKey());
			EntityDto entityDto = getEntityDto(tableMetaData, columnMetaDatas, commonDto);
			generateEntity(entityDto);

			BaseRespBodyDto baseRespBodyDto = getBaseRespBodyDto(tableMetaData, columnMetaDatas);
			generateBaseRespBody(baseRespBodyDto);

			BaseMapperDto baseMapperDto = getBaseMapperDto(tableMetaData, entityDto, baseRespBodyDto, commonDto);
			generateBaseMapper(baseMapperDto);
		}

	}

	public static BaseMapperDto getBaseMapperDto(TableMetaDataDto tableMetaData, EntityDto entityDto,
			BaseRespBodyDto baseRespBodyDto, CommonDto commonDto) {
		BaseMapperDto baseMapperDto = new BaseMapperDto();
		baseMapperDto.setCommon(commonDto);
		baseMapperDto.setPackageName(Config.MAIN_CLASS_PACKAGE + Config.MAPPER_PACKAGE_SUFFIX);
		baseMapperDto.setTableComment(tableMetaData.getComment());
		baseMapperDto.setClassName(JavaTypeUtil.getMapperName(tableMetaData.getName()));

		baseMapperDto.setEntityClassName(entityDto.getClassName());
		baseMapperDto.setImportEntityClass(entityDto.getPackageName() + "." + entityDto.getClassName());

		baseMapperDto.setBaseRespBodyClassName(baseRespBodyDto.getClassName());
		baseMapperDto
				.setImportBaseRespBodyClass(baseRespBodyDto.getPackageName() + "." + baseRespBodyDto.getClassName());
		return baseMapperDto;
	}

	public static void generateBaseMapper(BaseMapperDto baseMapperDto) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(baseMapperDto, "BaseMapper.ftl");
		String filePath = "D:/logs/" + baseMapperDto.getPackageName().replaceAll("\\.", "/") + "/"
				+ baseMapperDto.getClassName() + ".java";
		FileUtils.writeStringToFile(new File(filePath), code, StandardCharsets.UTF_8.name());
	}

	public static void generateEntity(EntityDto entityDto) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(entityDto, "Entity.ftl");
		String filePath = "D:/logs/" + entityDto.getPackageName().replaceAll("\\.", "/") + "/"
				+ entityDto.getClassName() + ".java";
		FileUtils.writeStringToFile(new File(filePath), code, StandardCharsets.UTF_8.name());
	}

	public static String getBaseRespBodyPackage() {
		int index = Config.MAIN_CLASS_PACKAGE.lastIndexOf('.');

		return Config.MAIN_CLASS_PACKAGE.subSequence(0, index) + ".rpc" + Config.MAIN_CLASS_PACKAGE.substring(index)
				+ Config.BASE_RESPBODY_PACKAGE_SUFFIX;
	}

	public static BaseRespBodyDto getBaseRespBodyDto(TableMetaDataDto tableMetaData,
			List<ColumnMetaDataDto> columnMetaDatas) {
		BaseRespBodyDto baseRespBodyDto = new BaseRespBodyDto();
		baseRespBodyDto.setPackageName(getBaseRespBodyPackage());
		baseRespBodyDto.setTableComment(tableMetaData.getComment());
		baseRespBodyDto.setClassName(JavaTypeUtil.getBaseRespBodyName(tableMetaData.getName()));

		List<EntityAttributeDto> attributes = new ArrayList<>();
		baseRespBodyDto.setAttributes(attributes);
		for (ColumnMetaDataDto columnMetaData : columnMetaDatas) {
			EntityAttributeDto entityAttribute = new EntityAttributeDto();
			entityAttribute.setComment(columnMetaData.getComment());
			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute
					.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));

			attributes.add(entityAttribute);
		}
		return baseRespBodyDto;
	}

	public static void generateBaseRespBody(BaseRespBodyDto baseRespBodyDto) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(baseRespBodyDto, "BaseRespBody.ftl");
		String filePath = "D:/logs/" + baseRespBodyDto.getPackageName().replaceAll("\\.", "/") + "/"
				+ baseRespBodyDto.getClassName() + ".java";
		FileUtils.writeStringToFile(new File(filePath), code, StandardCharsets.UTF_8.name());
	}

	/**
	 * 获取待Entity信息
	 * 
	 * @param tableMetaData
	 * @param columnMetaDatas
	 * @param commonDto
	 * @return
	 */
	public static EntityDto getEntityDto(TableMetaDataDto tableMetaData, List<ColumnMetaDataDto> columnMetaDatas,
			CommonDto commonDto) {
		EntityDto entityDto = new EntityDto();
		entityDto.setCommon(commonDto);
		entityDto.setPackageName(Config.MAIN_CLASS_PACKAGE + Config.ENTITY_PACKAGE_SUFFIX);
		entityDto.setTableName(tableMetaData.getName());
		entityDto.setTableComment(tableMetaData.getComment());
		entityDto.setClassName(JavaTypeUtil.getEntityName(tableMetaData.getName()));

		List<EntityAttributeDto> attributes = new ArrayList<>();
		entityDto.setAttributes(attributes);
		for (ColumnMetaDataDto columnMetaData : columnMetaDatas) {
			EntityAttributeDto entityAttribute = new EntityAttributeDto();
			entityAttribute.setComment(columnMetaData.getComment());
			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute
					.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));

			attributes.add(entityAttribute);
		}
		return entityDto;
	}

}