package org.smartframework.cloud.starter.swagger.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.smartframework.cloud.starter.configure.properties.SwaggerProperties.UploadYapiProperties;
import org.smartframework.cloud.utility.HttpUtil;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @desc swagger docs上传yapi listener
 * @author liyulin
 * @date 2020/01/10
 */
@RequiredArgsConstructor
@Slf4j
public class UploadSwagger2YapiListener implements ApplicationListener<ApplicationStartedEvent> {

	private final String groupName;
	private final UploadYapiProperties uploadYapi;
	private final String port;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		if (uploadYapi == null || StringUtils.isBlank(uploadYapi.getProjectUrl())) {
			log.info("yapi upload url is null! ignore upload api docs!");
			return;
		}

		asyncUploadSwaggerDocs();
	}

	/**
	 * 异步上传接口文档
	 */
	private void asyncUploadSwaggerDocs() {
		new Thread(() -> {
			log.info("====upload swagger to yapi start====");
			try {
				List<NameValuePair> parameters = new ArrayList<>();
				// yapi接口文档
				// https://hellosean1025.github.io/yapi/openapi-doc.html#u670du52a1u7aefu6570u636eu5bfcu51650a3ca20id3du670du52a1u7aefu6570u636eu5bfcu51653e203ca3e
				parameters.add(new BasicNameValuePair("type", "swagger"));
				parameters.add(new BasicNameValuePair("merge", uploadYapi.getMerge()));
				parameters.add(new BasicNameValuePair("token", uploadYapi.getToken()));
				parameters.add(new BasicNameValuePair("json", fetchSwaggerDocsJson()));
				HttpUtil.postWithUrlEncoded(uploadYapi.getProjectUrl(), parameters);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			log.info("====upload swagger to yapi finish====");
		}).start();
	}

	/**
	 * 获取swagger docs json
	 * 
	 * @return
	 * @throws IOException
	 */
	private String fetchSwaggerDocsJson() throws IOException {
		return HttpUtil.get(getSwaggerUrl());
	}

	private String getSwaggerUrl() {
		String url = "http://localhost:port/v2/api-docs?group=" + groupName;
		return url.replace("port", port);
	}

}