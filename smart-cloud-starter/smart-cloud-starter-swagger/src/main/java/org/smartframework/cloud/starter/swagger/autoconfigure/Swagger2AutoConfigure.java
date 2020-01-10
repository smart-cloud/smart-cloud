package org.smartframework.cloud.starter.swagger.autoconfigure;

import static com.google.common.base.Predicates.or;

import java.util.ArrayList;
import java.util.List;

import org.smartframework.cloud.starter.common.constants.PackageConfig;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.configure.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.base.Predicate;

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
@ConditionalOnProperty(name="smart.swagger.enable", havingValue = "true")
public class Swagger2AutoConfigure {
	
	@Autowired
	private SmartProperties smartProperties;

	@Bean
	public Docket docket() {
		// ApiSelector
		List<Predicate<RequestHandler>> requestHandlers = new ArrayList<>();
		String[] basePackages = PackageConfig.getBasePackages();
		for (String basePackage : basePackages) {
			requestHandlers.add(RequestHandlerSelectors.basePackage(basePackage));
		}
		ApiSelector apiSelector = new ApiSelector(or(requestHandlers), PathSelectors.any());
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName(smartProperties.getSwagger().getGroupName())
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.apiInfo(apiInfo())
				.select()
				.apis(apiSelector.getRequestHandlerSelector())
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(buildParameters());
	}
	
	private List<Parameter> buildParameters(){
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new ParameterBuilder()
				.parameterType("header")
				.required(true)
				.name("smart-sign")
				.description("签名")
				.modelRef(new ModelRef("string"))
				.scalarExample("109ad1a8e05f8de345e6d780f09b001e97dc3d6fa9bbbe6936edb2b75a81864ac3b0b071e093af001fbffa479217540138b98f6f165e8246dd25a2536649f1f6")
				.build());
		parameters.add(new ParameterBuilder()
				.parameterType("header")
				.required(true)
				.name("smart-timestamp")
				.description("请求时间戳（单位秒）")
				.modelRef(new ModelRef("long"))
				.scalarExample(1555778393862L)
				.build());
		parameters.add(new ParameterBuilder()
				.parameterType("header")
				.required(true)
				.name("smart-token")
				.description("请求token")
				.modelRef(new ModelRef("string"))
				.scalarExample("4c2e22605001000rK")
				.build());
		parameters.add(new ParameterBuilder()
				.parameterType("header")
				.required(true)
				.name("smart-nonce")
				.description("请求流水号")
				.modelRef(new ModelRef("string"))
				.scalarExample("eb9f81e7cee1c000")
				.build());
        return parameters;
	}

	private ApiInfo apiInfo() {
		SwaggerProperties swagger = smartProperties.getSwagger();
		Contact contact = new Contact(swagger.getName(), swagger.getUrl(), swagger.getEmail());
		return new ApiInfoBuilder()
				.title(swagger.getTitle())
				.description(swagger.getDescription())
				.contact(contact)
				.version(smartProperties.getApi().getApiVersion())
				.build();
	}

}