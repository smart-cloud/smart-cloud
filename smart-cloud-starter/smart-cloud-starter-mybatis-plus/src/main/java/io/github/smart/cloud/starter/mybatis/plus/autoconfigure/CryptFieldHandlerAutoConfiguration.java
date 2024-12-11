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

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.github.smart.cloud.starter.core.util.ReflectionUtil;
import io.github.smart.cloud.starter.mybatis.plus.common.CryptField;
import io.github.smart.cloud.starter.mybatis.plus.handler.CryptFieldHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 自定义加解密handlerType处理
 *
 * @author collin
 * @date 2024-03-15
 */
@org.springframework.context.annotation.Configuration
public class CryptFieldHandlerAutoConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MybatisSqlSessionFactoryBean) {
            registerCryptFieldHandler(((MybatisSqlSessionFactoryBean) bean).getConfiguration());
        } else if (bean instanceof DefaultSqlSessionFactory) {
            registerCryptFieldHandler(((DefaultSqlSessionFactory) bean).getConfiguration());
        }

        return bean;
    }

    /**
     * 将继承{@link CryptField}的类注册，使用{@link CryptFieldHandler}处理
     *
     * @param configuration
     */
    private void registerCryptFieldHandler(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(CryptField.class, CryptFieldHandler.class);

        Set<Class<? extends CryptField>> classSet = ReflectionUtil.getSubTypesOf(CryptField.class);
        if (!CollectionUtils.isEmpty(classSet)) {
            classSet.forEach(c -> typeHandlerRegistry.register(c, CryptFieldHandler.class));
        }
    }

}