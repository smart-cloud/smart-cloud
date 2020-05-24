package org.smartframework.cloud.code.generate.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.bo.template.BaseMapperBO;
import org.smartframework.cloud.code.generate.bo.template.BaseRespVOBO;
import org.smartframework.cloud.code.generate.bo.template.ClassCommentBO;
import org.smartframework.cloud.code.generate.bo.template.EntityAttributeBO;
import org.smartframework.cloud.code.generate.bo.template.EntityBO;
import org.smartframework.cloud.code.generate.config.Config;

import lombok.experimental.UtilityClass;

/**
 * 模板BO工具类
 *
 * @author liyulin
 * @date 2019-07-15
 */
@UtilityClass
public class TemplateBOUtil {

	/**
	 * 获取公共信息（如生成时间、作者等）
	 * 
	 * @param author
	 * @return
	 */
	public static ClassCommentBO getClassCommentBO(String author) {
		ClassCommentBO classComment = new ClassCommentBO();
		classComment.setCreateDate(new SimpleDateFormat(Config.CREATEDATE_FORMAT).format(new Date()));
		classComment.setAuthor(author);
		return classComment;
	}

	/**
	 * 获取生成Entity所需的参数信息
	 * 
	 * @param tableMetaData
	 * @param columnMetaDatas
	 * @param classComment
	 * @param mainClassPackage
	 * @param mask
	 * @return
	 */
	public static EntityBO getEntityBO(TableMetaDataBO tableMetaData, List<ColumnMetaDataBO> columnMetaDatas,
			ClassCommentBO classComment, String mainClassPackage, Map<String, Map<String, String>> mask) {
		EntityBO entityBO = new EntityBO();
		entityBO.setClassComment(classComment);
		entityBO.setTableName(tableMetaData.getName());
		entityBO.setTableComment(tableMetaData.getComment());
		entityBO.setPackageName(mainClassPackage + Config.ENTITY_PACKAGE_SUFFIX);
		entityBO.setClassName(JavaTypeUtil.getEntityName(tableMetaData.getName()));

		List<EntityAttributeBO> attributes = new ArrayList<>();
		entityBO.setAttributes(attributes);
		List<String> importPackages = new ArrayList<>();
		entityBO.setImportPackages(importPackages);
		for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
			EntityAttributeBO entityAttribute = new EntityAttributeBO();
			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute.setColumnName(columnMetaData.getName());
			String comment = StringEscapeUtil.secapeComment(columnMetaData.getComment());
			entityAttribute.setComment(comment);
			// mask信息
			entityAttribute.setMaskRule(getMaskRule(mask, tableMetaData.getName(), columnMetaData.getName()));
			if (entityAttribute.getMaskRule() != null && !importPackages.contains(Config.MaskPackage.MASK_RULE)) {
				importPackages.add(Config.MaskPackage.MASK_RULE);
				importPackages.add(Config.MaskPackage.MASK_LOG);
			}
			
			entityAttribute
					.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));
			String importPackage = JavaTypeUtil.getImportPackage(columnMetaData.getJdbcType());
			if (importPackage != null) {
				importPackages.add(importPackage);
			}

			attributes.add(entityAttribute);
		}
		return entityBO;
	}

	private static String getMaskRule(Map<String, Map<String, String>> mask, String tableName, String column) {
		if (mask == null) {
			return null;
		}
		Map<String, String> maskRuleMap = mask.get(tableName);
		if (maskRuleMap == null) {
			return null;
		}
		return maskRuleMap.get(column);
	}

	/**
	 * 获取生成BaseRespBody所需的参数信息
	 * 
	 * @param tableMetaData
	 * @param columnMetaDatas
	 * @param mainClassPackage
	 * @param importPackages
	 * @param mask
	 * @return
	 */
	public static BaseRespVOBO getBaseRespBodyBO(TableMetaDataBO tableMetaData, List<ColumnMetaDataBO> columnMetaDatas,
			String mainClassPackage, List<String> importPackages, Map<String, Map<String, String>> mask) {
		BaseRespVOBO baseRespVOBO = new BaseRespVOBO();
		baseRespVOBO.setTableComment(tableMetaData.getComment());
		baseRespVOBO.setPackageName(getBaseRespBodyPackage(mainClassPackage));
		baseRespVOBO.setClassName(JavaTypeUtil.getBaseRespBodyName(tableMetaData.getName()));
		baseRespVOBO.setImportPackages(importPackages);

		List<EntityAttributeBO> attributes = new ArrayList<>();
		baseRespVOBO.setAttributes(attributes);
		for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
			EntityAttributeBO entityAttribute = new EntityAttributeBO();
			String comment = StringEscapeUtil.secapeComment(columnMetaData.getComment());
			entityAttribute.setComment(comment);
			entityAttribute.setMaskRule(getMaskRule(mask, tableMetaData.getName(), columnMetaData.getName()));

			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute
					.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));

			attributes.add(entityAttribute);
		}
		return baseRespVOBO;
	}

	/**
	 * 获取BaseRespBody包名
	 * 
	 * @param mainClassPackage
	 * @return
	 */
	private static String getBaseRespBodyPackage(String mainClassPackage) {
		int index = mainClassPackage.lastIndexOf('.');

		return mainClassPackage.subSequence(0, index) + ".rpc" + mainClassPackage.substring(index)
				+ Config.BASE_RESPVO_PACKAGE_SUFFIX;
	}

	/**
	 * 获取生成BaesMapper所需的参数信息
	 * 
	 * @param tableMetaData
	 * @param entityBO
	 * @param baseRespVOBO
	 * @param classComment
	 * @param mainClassPackage
	 * @return
	 */
	public static BaseMapperBO getBaseMapperBO(TableMetaDataBO tableMetaData, EntityBO entityBO,
			BaseRespVOBO baseRespVOBO, ClassCommentBO classComment, String mainClassPackage) {
		BaseMapperBO baseMapperBO = new BaseMapperBO();
		baseMapperBO.setClassComment(classComment);
		baseMapperBO.setTableComment(tableMetaData.getComment());
		baseMapperBO.setPackageName(mainClassPackage + Config.MAPPER_PACKAGE_SUFFIX);
		baseMapperBO.setClassName(JavaTypeUtil.getMapperName(tableMetaData.getName()));

		List<String> importPackages = new ArrayList<>();
		// entity package
		importPackages.add(entityBO.getPackageName() + "." + entityBO.getClassName());
		// baseRespBody package
		importPackages.add(baseRespVOBO.getPackageName() + "." + baseRespVOBO.getClassName());
		baseMapperBO.setImportPackages(importPackages);
		
		baseMapperBO.setEntityClassName(entityBO.getClassName());
		baseMapperBO.setBaseRespBodyClassName(baseRespVOBO.getClassName());
		return baseMapperBO;
	}

}