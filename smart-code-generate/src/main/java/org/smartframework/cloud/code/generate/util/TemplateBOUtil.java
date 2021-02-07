package org.smartframework.cloud.code.generate.util;

import lombok.experimental.UtilityClass;
import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.bo.template.*;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.properties.DatasourceProperties;

import java.text.SimpleDateFormat;
import java.util.*;

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
        Set<String> importPackages = new HashSet<>(2);
        entityBO.setImportPackages(importPackages);
        for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
            EntityAttributeBO entityAttribute = new EntityAttributeBO();
            entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
            entityAttribute.setColumnName(columnMetaData.getName());
            entityAttribute.setComment(columnMetaData.getComment());
            // mask信息
            entityAttribute.setMaskRule(getMaskRule(mask, tableMetaData.getName(), columnMetaData.getName()));
            if (entityAttribute.getMaskRule() != null && !importPackages.contains(Config.MaskPackage.MASK_RULE)) {
                importPackages.add(Config.MaskPackage.MASK_RULE);
                importPackages.add(Config.MaskPackage.MASK_LOG);
            }

            entityAttribute.setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));
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
     * @param classComment
     * @param mainClassPackage
     * @param importPackages
     * @param mask
     * @return
     */
    public static BaseRespVOBO getBaseRespBodyBO(TableMetaDataBO tableMetaData, List<ColumnMetaDataBO> columnMetaDatas, ClassCommentBO classComment,
                                                 String mainClassPackage, Set<String> importPackages, Map<String, Map<String, String>> mask) {
        BaseRespVOBO baseRespVOBO = new BaseRespVOBO();
        baseRespVOBO.setClassComment(classComment);
        baseRespVOBO.setTableComment(tableMetaData.getComment());
        baseRespVOBO.setPackageName(getBaseRespBodyPackage(mainClassPackage));
        baseRespVOBO.setClassName(JavaTypeUtil.getBaseRespBodyName(tableMetaData.getName()));
        baseRespVOBO.setImportPackages(importPackages);

        List<EntityAttributeBO> attributes = new ArrayList<>();
        baseRespVOBO.setAttributes(attributes);
        for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
            EntityAttributeBO entityAttribute = new EntityAttributeBO();
            entityAttribute.setComment(columnMetaData.getComment());
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
     * @param datasource
     * @return
     */
    public static BaseMapperBO getBaseMapperBO(TableMetaDataBO tableMetaData, EntityBO entityBO,
                                               BaseRespVOBO baseRespVOBO, ClassCommentBO classComment, String mainClassPackage, DatasourceProperties datasource) {
        BaseMapperBO baseMapperBO = new BaseMapperBO();
        baseMapperBO.setClassComment(classComment);
        baseMapperBO.setTableComment(tableMetaData.getComment());
        baseMapperBO.setPackageName(mainClassPackage + Config.MAPPER_PACKAGE_SUFFIX);
        baseMapperBO.setClassName(JavaTypeUtil.getMapperName(tableMetaData.getName()));

        Set<String> importPackages = new HashSet<>(2);
        // entity package
        importPackages.add(entityBO.getPackageName() + "." + entityBO.getClassName());
        if (datasource != null) {
            importPackages.add(datasource.getDatasourceNamePackage());
            baseMapperBO.setDsValue(datasource.getValue());
        }
        baseMapperBO.setImportPackages(importPackages);

        baseMapperBO.setEntityClassName(entityBO.getClassName());
        baseMapperBO.setBaseRespBodyClassName(baseRespVOBO.getClassName());
        return baseMapperBO;
    }

}