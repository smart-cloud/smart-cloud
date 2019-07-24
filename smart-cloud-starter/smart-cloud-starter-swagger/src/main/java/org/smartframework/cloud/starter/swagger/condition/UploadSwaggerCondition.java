package org.smartframework.cloud.starter.swagger.condition;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.context.WebApplicationContext;

/**
 * 上传swagger文档的条件
 *
 * @author liyulin
 * @date 2019年7月24日下午11:54:57
 */
public class UploadSwaggerCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		ApplicationContext applicationContext = (ApplicationContext) context.getResourceLoader();
		return !isMockEnv(applicationContext);
	}

	/**
	 * 判断是否是集成测试环境
	 * 
	 * @return
	 */
	private boolean isMockEnv(ApplicationContext applicationContext) {
		if (applicationContext instanceof WebApplicationContext) {
			WebApplicationContext webApplicationContext = (WebApplicationContext) applicationContext;
			ServletContext servletContext = webApplicationContext.getServletContext();
			if (null != servletContext) {
				String servletContextName = servletContext.getServletContextName();
				if (null != servletContextName && servletContextName.toLowerCase().contains("mock")) {
					return true;
				}
			}
		}

		return false;
	}

}