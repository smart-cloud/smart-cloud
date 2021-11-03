package org.smartframework.cloud.starter.configure;

import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * smart cloud starter配置属性
 *
 * @author collin
 * @date 2021-11-11
 */
@Configuration
@EnableConfigurationProperties(SmartProperties.class)
@RefreshScope
public class SmartAutoConfiguration {

}