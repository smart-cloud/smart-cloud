package org.smartframework.cloud.starter.web.validation;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * hibernate-validator校验单例
 *
 * @author collin
 * @date 2021-10-31
 */
@UtilityClass
public class ValidatorSingleton {

    public static Validator getInstance() {
        return Holder.INSTANCE.getValidator();
    }

    @Getter
    public enum Holder {
        /**
         * hibernate-validator校验实例
         */
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