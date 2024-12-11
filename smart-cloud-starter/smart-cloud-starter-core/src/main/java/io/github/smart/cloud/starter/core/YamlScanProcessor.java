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
package io.github.smart.cloud.starter.core;

import io.github.smart.cloud.starter.core.annotation.YamlScan;
import io.github.smart.cloud.starter.core.constants.SmartApplicationConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link YamlScan}注解处理逻辑
 *
 * @author collin
 * @date 2019-06-21
 */
public class YamlScanProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        YamlLoader.loadYaml(environment, SmartApplicationConfig.getMainApplicationClass());
    }

    /**
     * @author collin
     * @desc yaml文件加载
     * @date 2020/02/23
     */
    private static class YamlLoader {

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
            if (yamlScan == null) {
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
                YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
                for (Resource resource : resourceSet) {
                    System.out.println("load yaml ==> " + resource.getFilename());

                    List<PropertySource<?>> propertySources = yamlPropertySourceLoader.load(resource.getFilename(), resource);
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