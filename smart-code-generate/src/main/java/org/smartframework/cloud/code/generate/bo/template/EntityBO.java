package org.smartframework.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Entity参数信息
 *
 * @author collin
 * @date 2021-11-02
 */
@Getter
@Setter
@ToString
public class EntityBO extends CommonBO {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 表字段对应的java属性信息
     */
	private List<EntityAttributeBO> attributes;

}