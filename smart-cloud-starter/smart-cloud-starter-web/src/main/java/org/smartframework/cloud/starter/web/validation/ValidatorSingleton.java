package org.smartframework.cloud.starter.web.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorSingleton {

	public static Validator getInstance() {
		return Holder.INSTANCE.getValidator();
	}

	@Getter
	public enum Holder {
		INSTANCE;
		private Validator validator;

		private Holder() {
			ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
					.configure()
					.addProperty("hibernate.validator.fail_fast", "true")
					.buildValidatorFactory();
			validator = validatorFactory.getValidator();
		}
	}

}