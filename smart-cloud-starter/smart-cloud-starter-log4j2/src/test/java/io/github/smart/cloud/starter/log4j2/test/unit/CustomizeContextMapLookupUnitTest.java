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
package io.github.smart.cloud.starter.log4j2.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.smart.cloud.starter.log4j2.enums.ExtProperty;
import io.github.smart.cloud.starter.log4j2.plugin.CustomizeContextMapLookup;

import java.util.Map;

class CustomizeContextMapLookupUnitTest {

    private String projectName = "smart-cloud-starter-log4j2";

    @Test
    void testReadAppNameFromProject() {
        CustomizeContextMapLookup customizeContextMapLookup = new CustomizeContextMapLookup();
        Assertions.assertThat(customizeContextMapLookup.lookup(ExtProperty.APP_NAME.getName())).isEqualTo(projectName);
    }

    @Test
    void testReadAppNameFromYaml() {
        Map<String, String> data = CustomizeContextMapLookup.init("name_test.yaml");
        Assertions.assertThat(data).containsEntry(ExtProperty.APP_NAME.getName(), "unit_test");
    }

    @Test
    void testReadAppNameFromYamlAndProjectName() {
        Map<String, String> data = CustomizeContextMapLookup.init("name_test_project.yaml");
        Assertions.assertThat(data).containsEntry(ExtProperty.APP_NAME.getName(), projectName);
    }

}