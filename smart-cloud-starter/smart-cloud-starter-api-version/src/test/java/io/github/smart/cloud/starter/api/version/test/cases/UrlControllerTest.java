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
package io.github.smart.cloud.starter.api.version.test.cases;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.smart.cloud.starter.api.version.test.prepare.vo.ApiVersionDemoRequestVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlControllerTest extends AbstractTest {

    @Test
    void testV1() throws Exception {
        String response = super.post("/client/url/v1/test", null, new ApiVersionDemoRequestVO("测试"), new TypeReference<String>() {
        });
        Assertions.assertThat(response).isNotBlank();
        Assertions.assertThat(response).isEqualTo("测试v1");
    }

    @Test
    void testV2() throws Exception {
        String response = super.post("/client/url/v2/test", null, new ApiVersionDemoRequestVO("测试"), new TypeReference<String>() {
        });
        Assertions.assertThat(response).isNotBlank();
        Assertions.assertThat(response).isEqualTo("测试v2");
    }

    @Test
    void testV3() throws Exception {
        String response = super.post("/client/url/v3/test", null, new ApiVersionDemoRequestVO("测试"), new TypeReference<String>() {
        });
        Assertions.assertThat(response).isNotBlank();
        Assertions.assertThat(response).isEqualTo("测试v3");
    }

    @Test
    void testV4() throws Exception {
        String response = super.post("/client/url/v4/test", null, new ApiVersionDemoRequestVO("测试"), new TypeReference<String>() {
        });
        Assertions.assertThat(response).isNotBlank();
        Assertions.assertThat(response).isEqualTo("测试v4");
    }

}