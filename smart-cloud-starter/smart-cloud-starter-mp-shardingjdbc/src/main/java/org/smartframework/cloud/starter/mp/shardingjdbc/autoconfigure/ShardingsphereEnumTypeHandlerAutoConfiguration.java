package org.smartframework.cloud.starter.mp.shardingjdbc.autoconfigure;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.smartframework.cloud.starter.core.business.util.ReflectionUtil;
import org.smartframework.cloud.starter.mp.shardingjdbc.handler.ShardingsphereEnumTypeHandler;
import org.smartframework.cloud.starter.mybatis.plus.enums.DeleteState;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Set;

/**
 * 因shardingsphere不支持枚举，故自定义{@link ShardingsphereEnumTypeHandler}兼容枚举
 *
 * @author collin
 * @date 2021-10-19
 */
@org.springframework.context.annotation.Configuration
public class ShardingsphereEnumTypeHandlerAutoConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MybatisSqlSessionFactoryBean) {
            registerShardingsphereEnumTypeHandler(((MybatisSqlSessionFactoryBean) bean).getConfiguration());
        } else if (bean instanceof DefaultSqlSessionFactory) {
            registerShardingsphereEnumTypeHandler(((DefaultSqlSessionFactory) bean).getConfiguration());
        }

        return bean;
    }

    /**
     * 将继承{@link IEnum}的类注册，使用{@link ShardingsphereEnumTypeHandler}处理
     *
     * @param configuration
     */
    private void registerShardingsphereEnumTypeHandler(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(DeleteState.class, ShardingsphereEnumTypeHandler.class);

        Set<Class<? extends IEnum>> classSet = ReflectionUtil.getSubTypesOf(IEnum.class);
        if (CollectionUtils.isNotEmpty(classSet)) {
            classSet.forEach(c -> typeHandlerRegistry.register(c, ShardingsphereEnumTypeHandler.class));
        }
    }

}