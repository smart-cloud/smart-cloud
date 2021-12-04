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
package org.smartframework.cloud.code.generate.test.cases;

import org.junit.jupiter.api.Test;
import org.smartframework.cloud.code.generate.core.CodeGenerateUtil;

class CodeFileGenerateUtilTest {

    @Test
    void testInit() throws Exception {
        CodeGenerateUtil.init();
    }

    @Test
    void testUser() throws Exception {
        CodeGenerateUtil.init("config/basic_user.yaml");
    }

    @Test
    void testOrder() throws Exception {
        CodeGenerateUtil.init("config/mall_order.yaml");
    }

    @Test
    void testProduct() throws Exception {
        CodeGenerateUtil.init("config/mall_product.yaml");
    }

}