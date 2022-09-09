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

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.prepare.service.OrderService;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.prepare.service.ProductService;
import org.assertj.core.api.Assertions;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

class DynamicElasticsearchTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @Test
    void testCreate() throws IOException {
        String indexName = "risk_order";
        CreateIndexResponse createOrderIndexResponse = orderService.createIndex(indexName);
        Assertions.assertThat(createOrderIndexResponse).isNotNull();
        Assertions.assertThat(createOrderIndexResponse.isAcknowledged()).isTrue();

        CreateIndexResponse createOrderIndexResponse2 = orderService.createIndex(indexName);
        Assertions.assertThat(createOrderIndexResponse2).isNotNull();
        Assertions.assertThat(createOrderIndexResponse2.isAcknowledged()).isFalse();

        CreateIndexResponse createProductIndexResponse = productService.createIndex("risk_product");
        Assertions.assertThat(createProductIndexResponse).isNotNull();
        Assertions.assertThat(createProductIndexResponse.isAcknowledged()).isTrue();
    }

}