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

import io.github.smart.cloud.starter.core.annotation.SmartBootApplication;
import io.github.smart.cloud.starter.core.constants.SmartApplicationConfig;
import io.github.smart.cloud.starter.core.constants.SmartEnv;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.test.context.AnnotatedClassFinder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 应用配置设置
 *
 * @author collin
 * @date 2024-12-11
 */
public class SmartApplicationConfigInitializer implements SpringApplicationRunListener {

    public SmartApplicationConfigInitializer(SpringApplication application, String[] args) {
        Class<?> mainApplicationClass = application.getMainApplicationClass();
        // 1、获取启动类、启动类注解
        SmartBootApplication smartBootApplication = AnnotationUtils.findAnnotation(mainApplicationClass, SmartBootApplication.class);
        if (smartBootApplication == null) {
            SpringBootTest springBootTest = AnnotationUtils.findAnnotation(mainApplicationClass, SpringBootTest.class);
            if (springBootTest != null && ArrayUtils.isNotEmpty(springBootTest.classes())) {
                mainApplicationClass = springBootTest.classes()[0];
            } else {
                // 此处findFromClass的参数为测试启动类
                mainApplicationClass = new AnnotatedClassFinder(SmartBootApplication.class).findFromClass(mainApplicationClass);
                if (mainApplicationClass == null) {
                    return;
                }
            }
            smartBootApplication = AnnotationUtils.findAnnotation(mainApplicationClass, SmartBootApplication.class);
            if (smartBootApplication != null) {
                SmartEnv.setUnitTest(true);
            }
        }
        if (smartBootApplication == null) {
            return;
        }

        SmartApplicationConfig.setMainApplicationClass(mainApplicationClass);

        // 设置{@link ComponentScan}的{@code basePackages}
        BasePackagesInitializer.init(mainApplicationClass, smartBootApplication);
    }

    /**
     * @author collin
     * @desc BasePackages初始化
     * @date 2020/02/23
     */
    private static class BasePackagesInitializer {

        /**
         * 设置{@link ComponentScan}的{@code basePackages}
         *
         * @param mainApplicationClass
         * @param smartBootApplication
         */
        public static void init(Class<?> mainApplicationClass, SmartBootApplication smartBootApplication) {
            String[] componentBasePackages = smartBootApplication.componentBasePackages();
            String[] basePackages = null;
            if (componentBasePackages == null || componentBasePackages.length == 0) {
                basePackages = new String[]{mainApplicationClass.getPackage().getName()};
            } else {
                basePackages = componentBasePackages;
            }
            SmartApplicationConfig.setBasePackages(basePackages);
        }

    }

}