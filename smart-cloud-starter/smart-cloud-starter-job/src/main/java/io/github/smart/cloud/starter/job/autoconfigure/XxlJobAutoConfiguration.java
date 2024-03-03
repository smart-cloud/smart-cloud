/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.job.autoconfigure;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import io.github.smart.cloud.starter.configure.SmartAutoConfiguration;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.configure.properties.XxlJobProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job自动配置
 * 
 * @author collin
 * @date 2019-07-11
 */
@Configuration
@AutoConfigureAfter(SmartAutoConfiguration.class)
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