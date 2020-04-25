package org.smartframework.cloud.utility.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类注入
 * 
 * @author liyulin
 * @date 2019-07-12
 */
@Configuration
public class UtilAutoConfigure {

	@Bean
	public SpringContextUtil springContextUtil() {
		return new SpringContextUtil();
	}

	@Bean
	public I18NUtil i18NUtil() {
		return new I18NUtil();
	}
	
}