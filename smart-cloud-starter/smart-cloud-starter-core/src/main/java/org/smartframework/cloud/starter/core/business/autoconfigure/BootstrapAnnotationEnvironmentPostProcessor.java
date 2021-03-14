package org.smartframework.cloud.starter.core.business.autoconfigure;

import org.apache.commons.lang3.ArrayUtils;
import org.smartframework.cloud.starter.core.business.util.AnnotatedClassFinder;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.core.support.annotation.SmartSpringCloudApplication;
import org.smartframework.cloud.starter.core.support.annotation.YamlScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 启动类注解值读取
 *
 * @author liyulin
 * @date 2019-06-21
 */
public class BootstrapAnnotationEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (isRegisterShutdownHook(application)) {
            Class<?> mainApplicationClass = application.getMainApplicationClass();
            // 1、获取启动类、启动类注解
            SmartSpringCloudApplication smartSpringCloudApplication = AnnotationUtils
                    .findAnnotation(mainApplicationClass, SmartSpringCloudApplication.class);
            if (smartSpringCloudApplication == null) {
                // 此处findFromClass的参数为测试启动类
                mainApplicationClass = new AnnotatedClassFinder(SmartSpringCloudApplication.class)
                        .findFromClass(mainApplicationClass);
                if (mainApplicationClass == null) {
                    return;
                }
                smartSpringCloudApplication = AnnotationUtils.findAnnotation(mainApplicationClass,
                        SmartSpringCloudApplication.class);
            }
            if (smartSpringCloudApplication == null) {
                return;
            }

            // 2、设置{@link ComponentScan}的{@code basePackages}
            BasePackagesInitializer.init(smartSpringCloudApplication);

            // 3、加载yml
            YamlLoader.loadYaml(environment, mainApplicationClass);
        }
    }

    private boolean isRegisterShutdownHook(SpringApplication application) {
        boolean registerShutdownHook = false;
        try {
            Field field = SpringApplication.class.getDeclaredField("registerShutdownHook");
            field.setAccessible(true);
            registerShutdownHook = field.getBoolean(application);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return registerShutdownHook;
    }

    /**
     * @author collin
     * @desc BasePackages初始化
     * @date 2020/02/23
     */
    static class BasePackagesInitializer {

        /**
         * 设置{@link ComponentScan}的{@code basePackages}
         *
         * @param smartSpringCloudApplication
         */
        public static void init(SmartSpringCloudApplication smartSpringCloudApplication) {
            String[] componentBasePackages = smartSpringCloudApplication.componentBasePackages();
            PackageConfig.setBasePackages(componentBasePackages);
        }

    }

    /**
     * @author collin
     * @desc yaml文件加载
     * @date 2020/02/23
     */
    static class YamlLoader {

        /**
         * 将启动类注解上配置的yaml文件加到environment中
         *
         * @param environment
         * @param mainApplicationClass
         */
        public static void loadYaml(ConfigurableEnvironment environment, Class<?> mainApplicationClass) {
            String[] locationPatterns = getLocationPatternsOnSpringBoot(mainApplicationClass);
            if (ArrayUtils.isEmpty(locationPatterns)) {
                return;
            }

            loadYaml(locationPatterns, environment);
        }

        /**
         * case：application正常启动
         *
         * @param mainApplicationClass
         * @return
         */
        private static String[] getLocationPatternsOnSpringBoot(Class<?> mainApplicationClass) {
            YamlScan yamlScan = AnnotationUtils.findAnnotation(mainApplicationClass, YamlScan.class);
            if (Objects.isNull(yamlScan)) {
                return new String[0];
            }

            return yamlScan.locationPatterns();
        }

        /**
         * 将匹配的yaml文件加到environment中
         * <p>
         * <b>NOTE</b>：此时日志配置还没有加载，还打不了日志
         *
         * @param locationPatterns
         * @param environment
         */
        private static void loadYaml(String[] locationPatterns, ConfigurableEnvironment environment) {
            // 1、获取每个文件对应的Resource对象
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Set<Resource> resourceSet = new HashSet<>();
            for (String locationPattern : locationPatterns) {
                try {
                    Resource[] resources = resourcePatternResolver.getResources(locationPattern);
                    if (ArrayUtils.isNotEmpty(resources)) {
                        Collections.addAll(resourceSet, resources);
                    }
                } catch (IOException e) {
                    // 此处如果出现异常，用log打印将会失效。
                    e.printStackTrace();
                }
            }

            if (resourceSet.isEmpty()) {
                return;
            }

            // 2、将所有Resource加入Environment中
            try {
                for (Resource resource : resourceSet) {
                    System.out.println("load yaml ==> " + resource.getFilename());

                    YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
                    List<PropertySource<?>> propertySources = yamlPropertySourceLoader.load(resource.getFilename(),
                            resource);
                    for (PropertySource<?> propertySource : propertySources) {
                        environment.getPropertySources().addLast(propertySource);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}