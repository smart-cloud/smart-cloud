package org.smartframework.cloud.starter.job.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.configure.properties.XxlJobProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * xxl-job自动配置
 * 
 * @author liyulin
 * @date 2019-07-11
 */
@Configuration
@ConditionalOnProperty(name = "smart.xxlJob.enabled", havingValue = "true", matchIfMissing = true)
public class XxlJobAutoConfiguration {

	@Value("${spring.application.name}")
	private String appName;

	@Bean(initMethod = "start", destroyMethod = "destroy")
	public XxlJobSpringExecutor xxlJobSpringExecutor(final SmartProperties smartProperties) {
		XxlJobProperties xxlJobProperties = smartProperties.getXxlJob();

		XxlJobSpringExecutor xxlJobExecutor = new XxlJobSpringExecutor();
		xxlJobExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
		if (StringUtils.isNotEmpty(xxlJobProperties.getAppName())) {
			xxlJobExecutor.setAppname(xxlJobProperties.getAppName());
		} else {
			xxlJobExecutor.setAppname(appName);
		}
		if (StringUtils.isNotEmpty(xxlJobProperties.getIp())) {
			xxlJobExecutor.setIp(xxlJobProperties.getIp());
		}

		if (null != xxlJobProperties.getPort()) {
			xxlJobExecutor.setPort(xxlJobProperties.getPort());
		}

		xxlJobExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		if (StringUtils.isNotEmpty(xxlJobProperties.getLogPath())) {
			xxlJobExecutor.setLogPath(xxlJobProperties.getLogPath());
		}
		if (xxlJobProperties.getLogRetentionDays() != null) {
			xxlJobExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
		}

		return xxlJobExecutor;
	}

}