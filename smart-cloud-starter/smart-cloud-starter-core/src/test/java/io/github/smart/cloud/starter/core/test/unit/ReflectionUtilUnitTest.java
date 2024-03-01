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
package io.github.smart.cloud.starter.core.test.unit;

import io.github.smart.cloud.starter.core.business.util.ReflectionUtil;
import io.github.smart.cloud.starter.core.constants.PackageConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Set;

class ReflectionUtilUnitTest {

    @Test
    void testGetSubTypesOf() {
        PackageConfig.setBasePackages(new String[]{Animal.class.getPackage().getName()});
        Set<Class<? extends Animal>> set = ReflectionUtil.getSubTypesOf(Animal.class);
        Assertions.assertThat(set).hasSize(1);
    }

    @Test
    void testGetTypesAnnotatedWith() {
        PackageConfig.setBasePackages(new String[]{AnnotatedClassController.class.getPackage().getName()});
        Set<Class<?>> set = ReflectionUtil.getTypesAnnotatedWith(RestController.class);
        Assertions.assertThat(set).hasSize(1);
    }

    @Test
    void testGetMethodsAnnotatedWith() {
        PackageConfig.setBasePackages(new String[]{AnnotatedClassController.class.getPackage().getName()});
        Set<Method> set = ReflectionUtil.getMethodsAnnotatedWith(GetMapping.class);
        Assertions.assertThat(set).hasSize(1);
    }

    class Animal {
    }

    class Dog extends Animal {
    }

    @RestController
    class AnnotatedClassController {

        @GetMapping
        public String test() {
            return "ok";
        }

    }

}