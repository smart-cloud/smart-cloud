package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;

import java.util.Map;

/**
 * @author liyulin
 * @desc 待生成的代码信息
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class CodeProperties {
    /**
     * 代码类作者
     */
    private String author;
    /**
     * 生成类型 {@link GenerateTypeEnum}：1、数据库整个表全部生成；2、只生成指定的表；3、除了指定的表，全部生成
     */
    private Integer type;
    /**
     * 指定要生成的表，多个表用英文逗号（,）隔开
     */
    private String specifiedTables;
    /**
     * 多数据源配置
     */
    private DatasourceProperties datasource;
    /**
     * 表字段脱敏规则
     */
    private Map<String, Map<String, String>> mask;
    /**
     * 启动类包名
     */
    private String mainClassPackage;
    /**
     * 工程信息
     */
    private ProjectProperties project;
}