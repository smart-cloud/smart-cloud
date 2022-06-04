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
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.cases;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.prepare.Application;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.support.ElasticsearchMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public abstract class AbstractIntegrationTest {

    @BeforeAll
    static void startElasticsearchServer() {
        ElasticsearchMockServer.startMockServer(9200);
    }

    @AfterAll
    static void stopElasticsearch() {
        ElasticsearchMockServer.stop();
    }

}