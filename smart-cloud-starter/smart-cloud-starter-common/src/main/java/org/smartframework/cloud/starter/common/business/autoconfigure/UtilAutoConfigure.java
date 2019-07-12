package org.smartframework.cloud.starter.common.business.autoconfigure;

import org.smartframework.cloud.starter.common.business.util.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类注入
 * 
 * @author liyulin
 * @date 2019年7月12日 下午12:22:32
 */
@Configuration
public class UtilAutoConfigure {

	@Bean
	public SpringContextUtil springContextUtil() {
		return new SpringContextUtil();
	}
	
}