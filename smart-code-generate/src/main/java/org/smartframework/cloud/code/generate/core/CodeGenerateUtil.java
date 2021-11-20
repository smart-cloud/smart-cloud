package org.smartframework.cloud.code.generate.core;

import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.JSQLParserException;
import org.smartframework.cloud.code.generate.bo.ColumnMetaDataBO;
import org.smartframework.cloud.code.generate.bo.TableMetaDataBO;
import org.smartframework.cloud.code.generate.bo.template.BaseMapperBO;
import org.smartframework.cloud.code.generate.bo.template.BaseRespBO;
import org.smartframework.cloud.code.generate.bo.template.ClassCommentBO;
import org.smartframework.cloud.code.generate.bo.template.EntityBO;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.PathProperties;
import org.smartframework.cloud.code.generate.properties.YamlProperties;
import org.smartframework.cloud.code.generate.util.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
     * @throws JSQLParserException
     */
    public static void init() throws Exception {
        YamlProperties yamlProperties = YamlUtil.readYamlProperties();
        YamlPropertiesCheckUtil.check(yamlProperties);

        CodeProperties codeProperties = yamlProperties.getCode();
        try (Connection connnection = DbUtil.getConnection(yamlProperties.getDb());) {
            Map<String, TableMetaDataBO> tableMetaDataMap = DbUtil.getTablesMetaData(connnection, codeProperties);
            ClassCommentBO classComment = TemplateUtil.getClassCommentBO(codeProperties.getAuthor());

            for (Map.Entry<String, TableMetaDataBO> entry : tableMetaDataMap.entrySet()) {
                generateSingleTable(connnection.getCatalog(), entry.getValue(), connnection, classComment, codeProperties);
            }
        }
    }

    /**
     * 生成当个表的代码
     *
     * @param database      数据库名
     * @param tableMetaData
     * @param connnection
     * @param classComment  公共信息
     * @param code
     * @throws SQLException
     * @throws IOException
     * @throws JSQLParserException
     */
    private static void generateSingleTable(String database, TableMetaDataBO tableMetaData, Connection connnection,
                                            ClassCommentBO classComment, CodeProperties code) throws Exception {
        List<ColumnMetaDataBO> columnMetaDatas = DbUtil.getTableColumnMetaDatas(connnection, database,
                tableMetaData.getName());
        String mainClassPackage = code.getMainClassPackage();
        PathProperties pathProperties = code.getProject().getPath();
        String rpcPath = pathProperties.getRpc();
        String servicePath = pathProperties.getService();

        EntityBO entityBO = TemplateUtil.getEntityBO(tableMetaData, columnMetaDatas, classComment, mainClassPackage,
                code.getMask());
        CodeFileGenerateUtil.generateEntity(entityBO, servicePath);

        BaseRespBO baseResp = TemplateUtil.getBaseRespBodyBO(tableMetaData, columnMetaDatas, classComment, mainClassPackage,
                entityBO.getImportPackages(), code.getMask());
        CodeFileGenerateUtil.generateBaseRespVO(baseResp, rpcPath);

        BaseMapperBO baseMapperBO = TemplateUtil.getBaseMapperBO(tableMetaData, entityBO, baseResp,
                classComment, mainClassPackage);
        CodeFileGenerateUtil.generateBaseMapper(baseMapperBO, servicePath);
    }

}