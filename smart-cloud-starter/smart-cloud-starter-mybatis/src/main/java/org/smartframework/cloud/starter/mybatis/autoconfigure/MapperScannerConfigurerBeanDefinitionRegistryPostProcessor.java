package org.smartframework.cloud.starter.mybatis.autoconfigure;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 处理Mapper interface与启动类不在同一包名下时无法注入IOC容器的问题
 *
 * @author collin
 * @date 2021-01-28
 */
@Configuration
@AutoConfigureAfter(MapperScannerConfigurer.class)
public class MapperScannerConfigurerBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, Ordered {

    /**
     * MapperScannerConfigurer的属性basePackage
     */
    private String mapperPropertyName = "basePackage";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 处理Mapper interface与启动类不在同一包名下时无法注入IOC容器的问题
        String mapperScannerConfigurerBeanName = MapperScannerConfigurer.class.getName();
        if (!registry.containsBeanDefinition(mapperScannerConfigurerBeanName)) {
            return;
        }
        BeanDefinition beanDefinition = registry.getBeanDefinition(mapperScannerConfigurerBeanName);
        MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
        PropertyValue existedPropertyValue = mutablePropertyValues.getPropertyValue(mapperPropertyName);

        Set<String> packages = new HashSet<>(2);
        packages.add(String.valueOf(existedPropertyValue.getValue()));
        String[] componentScans = PackageConfig.getBasePackages();
        for (String componentScan : componentScans) {
            packages.add(componentScan);
        }
        // 将componentScan指定的包名也加入到mapper的扫描范围
        beanDefinition.getPropertyValues().add(mapperPropertyName, StringUtils.collectionToCommaDelimitedString(packages));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}