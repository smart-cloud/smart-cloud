package org.smartframework.cloud.starter.swagger.validators.plugins.parameter;

import static springfox.bean.validators.plugins.Validators.annotationFromParameter;

import javax.validation.constraints.NotEmpty;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
@Slf4j
public class NotEmptyAnnotationPlugin implements ParameterBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		// we simply support all documentationTypes!
		return true;
	}

	@Override
	public void apply(ParameterContext context) {
		Optional<NotEmpty> notEmpty = annotationFromParameter(context, NotEmpty.class);

		if (notEmpty.isPresent()) {
			log.debug("@NotEmpty present: setting parameter as required");
			context.parameterBuilder().allowEmptyValue(false).required(true);
		}
	}
}