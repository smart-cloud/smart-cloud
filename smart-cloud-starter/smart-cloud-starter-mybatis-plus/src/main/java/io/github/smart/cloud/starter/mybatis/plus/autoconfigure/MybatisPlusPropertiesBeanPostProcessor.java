/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.mybatis.plus.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import org.apache.commons.lang3.StringUtils;
import io.github.smart.cloud.constants.SymbolConstant;
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