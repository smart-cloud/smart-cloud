package org.smartframework.cloud.starter.swagger.core;

import java.io.IOException;

import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.github.swagger2markup.utils.URIUtils;
import lombok.RequiredArgsConstructor;

/**
 * swagger json转markdown
 *
 * @author liyulin
 * @date 2019-07-21
 */
@RequiredArgsConstructor
public class Swagger2Markdown {

	private final String port;
	private final String groupName;

	/**
	 * 获取markdown格式的api文档
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getSwaggerMd() throws IOException {
		Swagger2MarkupConfig swagger2MarkupConfig = new Swagger2MarkupConfigBuilder()
				.withMarkupLanguage(MarkupLanguage.MARKDOWN)
				.withOutputLanguage(Language.ZH)
				.withFlatBody()
				.withGeneratedExamples()
				.build();
		
		Swagger2MarkupConverter converter = Swagger2MarkupConverter
				.from(URIUtils.create(getSwaggerUrl()))
                .withConfig(swagger2MarkupConfig)
                .build();
		
		String md = converter.toString();
		
		// TODO:上传文档服务器
		return md;
	}
	
	private String getSwaggerUrl() {
		String url = "http://localhost:port/v2/api-docs?group=" + groupName;
		return url.replace("port", port);
	}

}