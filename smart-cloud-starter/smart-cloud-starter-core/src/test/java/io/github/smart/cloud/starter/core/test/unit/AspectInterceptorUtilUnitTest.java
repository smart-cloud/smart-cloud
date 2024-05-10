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

import io.github.smart.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

class AspectInterceptorUtilUnitTest {

    @Test
    void testGetTypeExpression() {
        String expression = AspectInterceptorUtil.getTypeExpression(Arrays.asList(Controller.class, RestController.class));
        Assertions.assertThat(expression).isEqualTo("@within(org.springframework.stereotype.Controller)||@within(org.springframework.web.bind.annotation.RestController)");
    }

    @Test
    void testGetMethodExpression() {
        String expression = AspectInterceptorUtil.getMethodExpression(Arrays.asList(Controller.class, RestController.class));
        Assertions.assertThat(expression).isEqualTo("@annotation(org.springframework.stereotype.Controller)||@annotation(org.springframework.web.bind.annotation.RestController)");
    }

    @Test
    void testGetApiAnnotations() {
        List<Class<? extends Annotation>> annotations = AspectInterceptorUtil.getApiAnnotations();
        Assertions.assertThat(annotations)
                .isNotEmpty()
                .contains(RequestMapping.class, GetMapping.class, PostMapping.class, DeleteMapping.class, PutMapping.class, PatchMapping.class);
    }

    @Test
    void testGetApiExpression() {
        String apiExpression = AspectInterceptorUtil.getApiExpression(new String[]{"org.smartcloud"});
        Assertions.assertThat(apiExpression)
                .isNotBlank()
                .isEqualTo("(execution( * org.smartcloud..*.*(..)))" +
                "&&(@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
                "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
                "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
                "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
                "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
                "||@annotation(org.springframework.web.bind.annotation.PatchMapping))");
    }

}