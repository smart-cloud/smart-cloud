package org.smartframework.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * BaseRespVO参数信息
 *
 * @author collin
 * @date 2021-11-04
 */
@Getter
@Setter
@ToString
public class BaseRespBO extends CommonBO {

    /**
     * 表字段对应的java属性信息
     */
    List<EntityAttributeBO> attributes;

}