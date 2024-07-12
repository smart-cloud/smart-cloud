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
package io.github.smart.cloud.utility.test.unit;

import io.github.smart.cloud.utility.ClassUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ClassUtilTest {

    @Test
    void testFindFieldValue() {
        String name = ClassUtil.findFieldValue(new TestClass(), TestClass.class, "name");
        Assertions.assertThat(name).isEqualTo("test");
    }

    @Test
    void testFindMethod() throws InvocationTargetException, IllegalAccessException {
        Method getAgeMethod = ClassUtil.findMethod(TestClass.class, "getAge");
        Assertions.assertThat(getAgeMethod.invoke(new TestClass())).isEqualTo(9);
    }

    static class TestClass {

        private String name = "test";

        private int getAge() {
            return 9;
        }

    }

}