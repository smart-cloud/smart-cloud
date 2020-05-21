package org.smartframework.cloud.starter.swagger.autoconfigure;

import static com.google.common.base.Predicates.or;

import java.util.ArrayList;
import java.util.List;

import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.configure.properties.SwaggerProperties;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.swagger.condition.UploadSwaggerCondition;
import org.smartframework.cloud.starter.swagger.listener.UploadSwagger2YapiListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.base.Predicate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2配置
 *
 * @author liyulin
 * @date 2019-04-22
 */
@Configuration
@EnableSwagger2
@Import(SmartBeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(name = "smart.swagger.enable", havingValue = "true")
public class Swagger2AutoConfigure {

	@Bean
	public SmartApiACOperationBuilderPlugin smartApiACOperationBuilderPlugin() {
		return new SmartApiACOperationBuilderPlugin();
	}

	@Bean
	public Docket docket(final SmartProperties smartProperties) {
		// ApiSelector
		List<Predicate<RequestHandler>> requestHandlers = new ArrayList<>();
		String[] basePackages = PackageConfig.getBasePackages();
		for (String basePackage : basePackages) {
			requestHandlers.add(RequestHandlerSelectors.basePackage(basePackage));
		}
		ApiSelector apiSelector = new ApiSelector(or(requestHandlers), PathSelectors.any());

		return new Docket(DocumentationType.SWAGGER_2).groupName(smartProperties.getSwagger().getGroupName())
				.genericModelSubstitutes(DeferredResult.class).useDefaultResponseMessages(false)
				.forCodeGeneration(false).apiInfo(apiInfo(smartProperties)).select().apis(apiSelector.getRequestHandlerSelector())
				.paths(PathSelectors.any()).build().globalOperationParameters(buildParameters());
	}

	@Configuration
	@Conditional(UploadSwaggerCondition.class)
	@ConditionalOnProperty(name = "smart.swagger.uploadYapi.enable", havingValue = "true", matchIfMissing = true)
	static class UploadSwaggerYapiAutoConfigure {

		@Bean
		public UploadSwagger2YapiListener uploadSwaggerListener(final SmartProperties smartProperties,
				@Value("${server.port}") String port) {
			SwaggerProperties swaggerProperties = smartProperties.getSwagger();
			return new UploadSwagger2YapiListener(swaggerProperties.getGroupName(), swaggerProperties.getUploadYapi(), port);
		}

	}

	private ApiInfo apiInfo(final SmartProperties smartProperties) {
		SwaggerProperties swagger = smartProperties.getSwagger();
		Contact contact = new Contact(swagger.getName(), swagger.getUrl(), swagger.getEmail());
		return new ApiInfoBuilder().title(swagger.getTitle()).description(swagger.getDescription()).contact(contact)
				.version(smartProperties.getApi().getApiVersion()).build();
	}

	private List<Parameter> buildParameters() {
		List<Parameter> parameters = new ArrayList<>();
		HeaderParameter[] headerParameters = HeaderParameter.values();
		for (HeaderParameter headerParameter : headerParameters) {
			parameters.add(new ParameterBuilder().parameterType(headerParameter.getParameterType())
					.required(headerParameter.isRequired()).name(headerParameter.getName())
					.description(headerParameter.getDescription()).modelRef(new ModelRef(headerParameter.getModelRef()))
					.scalarExample(headerParameter.getExample()).build());
		}
		return parameters;
	}

	@AllArgsConstructor
	@Getter
	enum HeaderParameter {
		SIGN("header", "smart-sign", true, "签名", "string",
				"109ad1a8e05f8de345e6d780f09b001e97dc3d6fa9bbbe6936edb2b75a81864ac3b0b071e093af001fbffa479217540138b98f6f165e8246dd25a2536649f1f6"),
		TIMESTAMP("header", "smart-timestamp", true, "请求时间戳（单位秒）", "long", "1555778393862"),
		TOKEN("header", "smart-token", true, "请求token", "string", "4c2e22605001000rK"),
		NONCE("header", "smart-nonce", true, "请求流水号", "string", "eb9f81e7cee1c000");

		private String parameterType;
		private String name;
		private boolean required;
		private String description;
		private String modelRef;
		private String example;
	}

}