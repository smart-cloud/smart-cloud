package org.smartframework.cloud.starter.mybatis.plus.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.smartframework.cloud.starter.mybatis.plus.enums.DeleteState;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * 修改{@link MybatisPlusProperties}bean的属性typeEnumsPackage，将{@link DeleteState}枚举的包名自动设置
 *
 * @author collin
 * @date 2021-10-12
 */
@Configuration
public class MybatisPlusPropertiesBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return setDeleteStateEnumPackage(bean);
    }

    /**
     * 设置{@link DeleteState}枚举的包名
     *
     * @param bean
     * @return
     */
    private Object setDeleteStateEnumPackage(Object bean) {
        if (!(bean instanceof MybatisPlusProperties)) {
            return bean;
        }

        String deleteStatePackage = DeleteState.class.getPackage().getName();

        MybatisPlusProperties mybatisPlusProperties = (MybatisPlusProperties) bean;
        String typeEnumsPackage = mybatisPlusProperties.getTypeEnumsPackage();
        if (StringUtils.isNotBlank(typeEnumsPackage)) {
            typeEnumsPackage += SymbolConstant.COMMA + deleteStatePackage;
        } else {
            typeEnumsPackage = deleteStatePackage;
        }
        mybatisPlusProperties.setTypeEnumsPackage(typeEnumsPackage);
        return bean;
    }

}