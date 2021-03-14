package org.smartframework.cloud.code.generate.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表字段元数据信息
 *
 * @author liyulin
 * @date 2019-07-15
 */
@Getter
@Setter
@ToString
public class ColumnMetaDataBO {

    /**
     * 表字段名
     */
    private String name;
    /**
     * 数据类型
     */
    private int jdbcType;
    /**
     * 字段备注
     */
    private String comment;
    /**
     * 字段长度
     */
    private int length;
    /**
     * 是否为主键
     */
    private boolean primaryKey;

}