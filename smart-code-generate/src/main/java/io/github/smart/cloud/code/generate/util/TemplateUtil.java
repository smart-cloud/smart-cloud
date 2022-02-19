/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.code.generate.util;

import io.github.smart.cloud.code.generate.bo.ColumnMetaDataBO;
import io.github.smart.cloud.code.generate.bo.TableMetaDataBO;
import io.github.smart.cloud.code.generate.bo.template.*;
import io.github.smart.cloud.code.generate.config.Config;
import io.github.smart.cloud.code.generate.enums.DefaultColumnEnum;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 模板BO工具类
 *
 * @author collin
 * @date 2019-07-15
 */
public class TemplateUtil {

    private TemplateUtil() {
    }

    /**
     * 获取公共信息（如生成时间、作者等）
     *
     * @param author
     * @return
     */
    public static ClassCommentBO getClassCommentBO(String author) {
        ClassCommentBO classComment = new ClassCommentBO();
        classComment.setCreateDate(new SimpleDateFormat(Config.CODE_CREATE_DATE_FORMAT).format(new Date()));
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
        entityBO.setTableName(TableUtil.getTableName(tableMetaData.getName()));
        entityBO.setTableComment(tableMetaData.getComment());
        entityBO.setPackageName(mainClassPackage + Config.ENTITY_PACKAGE_SUFFIX);
        entityBO.setClassName(JavaTypeUtil.getEntityName(tableMetaData.getName()));

        List<EntityAttributeBO> attributes = new ArrayList<>();
        entityBO.setAttributes(attributes);
        Set<String> importPackages = new HashSet<>(2);
        entityBO.setImportPackages(importPackages);
        for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
            if (DefaultColumnEnum.contains(columnMetaData.getName())) {
                continue;
            }
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
            entityAttribute.setPrimaryKey(columnMetaData.getPrimaryKey());
            if (columnMetaData.getPrimaryKey()) {
                importPackages.add("com.baomidou.mybatisplus.annotation.TableId");
            }
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
    public static BaseRespBO getBaseRespBodyBO(TableMetaDataBO tableMetaData, List<ColumnMetaDataBO> columnMetaDatas, ClassCommentBO classComment,
                                               String mainClassPackage, Set<String> importPackages, Map<String, Map<String, String>> mask) {
        BaseRespBO baseResp = new BaseRespBO();
        baseResp.setClassComment(classComment);
        baseResp.setTableComment(tableMetaData.getComment());
        baseResp.setPackageName(getBaseRespBodyPackage(mainClassPackage));
        baseResp.setClassName(JavaTypeUtil.getBaseRespBodyName(tableMetaData.getName()));
        baseResp.setImportPackages(importPackages);

        List<EntityAttributeBO> attributes = new ArrayList<>();
        baseResp.setAttributes(attributes);
        for (ColumnMetaDataBO columnMetaData : columnMetaDatas) {
            if (DefaultColumnEnum.contains(columnMetaData.getName())) {
                continue;
            }
            EntityAttributeBO entityAttribute = new EntityAttributeBO();
            entityAttribute.setComment(columnMetaData.getComment());
            entityAttribute.setMaskRule(getMaskRule(mask, tableMetaData.getName(), columnMetaData.getName()));

            entityAttribute.setName(TableUtil.getAttibuteName(columnMetaData.getName()));
            entityAttribute
                    .setJavaType(JavaTypeUtil.getByJdbcType(columnMetaData.getJdbcType(), columnMetaData.getLength()));

            attributes.add(entityAttribute);
        }
        return baseResp;
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
     * @param baseResp
     * @param classComment
     * @param mainClassPackage
     * @return
     */
    public static BaseMapperBO getBaseMapperBO(TableMetaDataBO tableMetaData, EntityBO entityBO,
                                               BaseRespBO baseResp, ClassCommentBO classComment, String mainClassPackage) {
        BaseMapperBO baseMapperBO = new BaseMapperBO();
        baseMapperBO.setClassComment(classComment);
        baseMapperBO.setTableComment(tableMetaData.getComment());
        baseMapperBO.setPackageName(mainClassPackage + Config.MAPPER_PACKAGE_SUFFIX);
        baseMapperBO.setClassName(JavaTypeUtil.getMapperName(tableMetaData.getName()));

        Set<String> importPackages = new HashSet<>(2);
        // entity package
        importPackages.add(entityBO.getPackageName() + "." + entityBO.getClassName());
        baseMapperBO.setImportPackages(importPackages);

        baseMapperBO.setEntityClassName(entityBO.getClassName());
        return baseMapperBO;
    }

}