package org.smartframework.cloud.starter.configure;

import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SmartProperties.class)
public class SmartAutoConfigure {

}