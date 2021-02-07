package org.smartframework.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseMapperBO extends CommonBO {

    /**
     * entity类名
     */
    private String entityClassName;
    /**
     * baseRespBody类名
     */
    private String baseRespBodyClassName;
    /**
     * 注解DS的value属性值
     */
    private String dsValue;

}