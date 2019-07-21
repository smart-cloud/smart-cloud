package org.smartframework.cloud.starter.swagger.listener;

import java.io.IOException;

import org.smartframework.cloud.starter.swagger.core.Swagger2Markdown;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务启动完上传文档
 *
 * @author liyulin
 * @date 2019-07-21
 */
@RequiredArgsConstructor
@Slf4j
public class SwaggerListener implements ApplicationListener<ApplicationStartedEvent> {

	private final Swagger2Markdown swagger2Markdown;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		syncUploadApiDocs();
	}

	/**
	 * 异步上传接口文档
	 */
	private void syncUploadApiDocs() {
		new Thread(() -> {
			try {
				swagger2Markdown.getSwaggerMd();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}).start();
	}

}