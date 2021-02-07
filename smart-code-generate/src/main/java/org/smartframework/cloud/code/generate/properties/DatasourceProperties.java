package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liyulin
 * @date 2021-02-08
 */
@Getter
@Setter
@ToString
public class DatasourceProperties {

    /**
     * mybatis plus多数据源DS注解的value值
     */
    private String value;
    /**
     * DataSourceName包名
     */
    private String datasourceNamePackage;

}