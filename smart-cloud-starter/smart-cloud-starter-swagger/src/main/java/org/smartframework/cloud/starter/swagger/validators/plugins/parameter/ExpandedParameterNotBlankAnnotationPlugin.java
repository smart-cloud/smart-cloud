package org.smartframework.cloud.starter.swagger.validators.plugins.parameter;

import javax.validation.constraints.NotBlank;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

@Component
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
@Slf4j
public class ExpandedParameterNotBlankAnnotationPlugin implements ExpandedParameterBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(ParameterExpansionContext context) {
		Optional<NotBlank> notBlank = context.findAnnotation(NotBlank.class);

		if (notBlank.isPresent()) {
			log.debug("Setting parameter to required because of @NotBlank attribute");
			context.getParameterBuilder().allowEmptyValue(false).required(true);
		}
	}

}