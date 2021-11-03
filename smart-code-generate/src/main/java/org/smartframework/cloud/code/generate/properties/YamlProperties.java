package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 代码生成yaml文件配置
 *
 * @author collin
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
@ConfigurationProperties
public class YamlProperties {
    /**
     * 数据库连接信息
     */
    private DbProperties db;
    /**
     * 待生成的代码信息
     */
    private CodeProperties code;
}