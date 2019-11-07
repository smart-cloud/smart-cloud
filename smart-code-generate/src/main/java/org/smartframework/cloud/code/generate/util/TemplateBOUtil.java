package org.smartframework.cloud.code.generate.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.bo.template.BaseMapperBO;
import org.smartframework.cloud.code.generate.bo.template.BaseRespBodyBO;
import org.smartframework.cloud.code.generate.bo.template.ClassCommentBO;
import org.smartframework.cloud.code.generate.bo.template.EntityAttributeBO;
import org.smartframework.cloud.code.generate.bo.template.EntityBO;
import org.smartframework.cloud.code.generate.config.Config;

import lombok.experimental.UtilityClass;

/**
 * 模板dto工具类
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
	 * @return
	 */
	public static EntityBO getEntityBO(TableMetaDataBO tableMetaData, List<ColumnMetaDataBO> columnMetaDatas,
			ClassCommentBO classComment, String mainClassPackage) {
		EntityBO entityBO = new EntityBO();
		entityBO.setClassComment(classComment);
		entityBO.setTableName(tableMetaData.getName());
		entityBO.setTableComment(tableMetaData.getComment());
		entityBO.setPackageName(mainClassPackage + Config.ENTITY_PACKAGE_SUFFIX);
		entityBO.setClassName(JavaTypeUtil.getEntityName(tableMetaData.getName()));

		List<EntityAttributeBO> attributes = new ArrayList<>();
		entityBO.setAttributes(attributes);
		Set<String> importPackages = new HashSet<>();
		entityBO.setImportPackages(importPackages);
		for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
			EntityAttributeBO entityAttribute = new EntityAttributeBO();
			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute.setColumnName(columnMetaData.getName());
			String comment = StringEscapeUtil.secapeComment(columnMetaData.getComment());
			entityAttribute.setComment(comment);
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

	/**
	 * 获取生成BaseRespBody所需的参数信息
	 * 
	 * @param tableMetaData
	 * @param columnMetaDatas
	 * @param mainClassPackage
	 * @param importPackages
	 * @return
	 */
	public static BaseRespBodyBO getBaseRespBodyBO(TableMetaDataBO tableMetaData,
			List<ColumnMetaDataBO> columnMetaDatas, String mainClassPackage, Set<String> importPackages) {
		BaseRespBodyBO baseRespBodyBO = new BaseRespBodyBO();
		baseRespBodyBO.setTableComment(tableMetaData.getComment());
		baseRespBodyBO.setPackageName(getBaseRespBodyPackage(mainClassPackage));
		baseRespBodyBO.setClassName(JavaTypeUtil.getBaseRespBodyName(tableMetaData.getName()));
		baseRespBodyBO.setImportPackages(importPackages);

		List<EntityAttributeBO> attributes = new ArrayList<>();
		baseRespBodyBO.setAttributes(attributes);
		for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
			EntityAttributeBO entityAttribute = new EntityAttributeBO();
			String comment = StringEscapeUtil.secapeComment(columnMetaData.getComment());
			entityAttribute.setComment(comment);

			entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
			entityAttribute
					.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));

			attributes.add(entityAttribute);
		}
		return baseRespBodyBO;
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
				+ Config.BASE_RESPBODY_PACKAGE_SUFFIX;
	}

	/**
	 * 获取生成BaesMapper所需的参数信息
	 * 
	 * @param tableMetaData
	 * @param entityBO
	 * @param baseRespBodyBO
	 * @param classComment
	 * @param mainClassPackage
	 * @return
	 */
	public static BaseMapperBO getBaseMapperBO(TableMetaDataBO tableMetaData, EntityBO entityBO,
			BaseRespBodyBO baseRespBodyBO, ClassCommentBO classComment, String mainClassPackage) {
		BaseMapperBO baseMapperBO = new BaseMapperBO();
		baseMapperBO.setClassComment(classComment);
		baseMapperBO.setTableComment(tableMetaData.getComment());
		baseMapperBO.setPackageName(mainClassPackage + Config.MAPPER_PACKAGE_SUFFIX);
		baseMapperBO.setClassName(JavaTypeUtil.getMapperName(tableMetaData.getName()));

		baseMapperBO.setEntityClassName(entityBO.getClassName());
		baseMapperBO.setImportEntityClass(entityBO.getPackageName() + "." + entityBO.getClassName());

		baseMapperBO.setBaseRespBodyClassName(baseRespBodyBO.getClassName());
		baseMapperBO
				.setImportBaseRespBodyClass(baseRespBodyBO.getPackageName() + "." + baseRespBodyBO.getClassName());
		return baseMapperBO;
	}

}