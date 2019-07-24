package org.smartframework.cloud.starter.swagger.autoconfigure;

import static com.google.common.base.Predicates.or;

import java.util.ArrayList;
import java.util.List;

import org.smartframework.cloud.starter.common.constants.PackageConfig;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.configure.properties.SwaggerProperties;
import org.smartframework.cloud.starter.swagger.condition.UploadSwaggerCondition;
import org.smartframework.cloud.starter.swagger.core.Swagger2Markdown;
import org.smartframework.cloud.starter.swagger.listener.SwaggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
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
@ConditionalOnProperty(name="smart.swagger.enable", havingValue = "true")
public class Swagger2AutoConfigure {
	
	@Autowired
	private SmartProperties smartProperties;

	@Bean
	public Docket docket() {
		List<Predicate<RequestHandler>> requestHandlers = new ArrayList<>();
		String[] basePackages = PackageConfig.getBasePackages();
		for (String basePackage : basePackages) {
			requestHandlers.add(RequestHandlerSelectors.basePackage(basePackage));
		}
		ApiSelector apiSelector = new ApiSelector(or(requestHandlers), PathSelectors.any());
		return new Docket(DocumentationType.SWAGGER_2).groupName(smartProperties.getSwagger().getGroupName())
				.genericModelSubstitutes(DeferredResult.class).useDefaultResponseMessages(false)
				.forCodeGeneration(false).apiInfo(apiInfo()).select()
				.apis(apiSelector.getRequestHandlerSelector()).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		SwaggerProperties swagger = smartProperties.getSwagger();
		Contact contact = new Contact(swagger.getName(), swagger.getUrl(),
				swagger.getEmail());
		return new ApiInfoBuilder()
				.title(swagger.getTitle())
				.description(swagger.getDescription())
				.contact(contact)
				.version(smartProperties.getApi().getApiVersion())
				.build();
	}

	@Configuration
	@Conditional(UploadSwaggerCondition.class)
	static class UploadSwaggerAutoConfigure {
		
		@Bean
		public Swagger2Markdown Swagger2Markdown(@Value("${server.port}") String port,
				final SmartProperties smartProperties) {
			return new Swagger2Markdown(port, smartProperties.getSwagger().getGroupName());
		}

		@Bean
		public SwaggerListener SwaggerListener(final Swagger2Markdown swagger2Markdown) {
			return new SwaggerListener(swagger2Markdown);
		}
		
	}

}