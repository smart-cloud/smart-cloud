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
package org.smartframework.cloud.utility.test.unit.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.test.unit.jackson.bo.Product;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class JacksonUtilTest {

    @Test
    void testToJson() {
        Product product1 = buildProduct();
        String productStr = JacksonUtil.toJson(product1);
        Assertions.assertThat(productStr).isNotBlank();

        Product product2 = JacksonUtil.parseObject(productStr, Product.class);
        Assertions.assertThat(product1.getId()).isEqualTo(product2.getId());
        Assertions.assertThat(product1.getName()).isEqualTo(product2.getName());
        Assertions.assertThat(product1.getPrice()).isEqualTo(product2.getPrice());
    }

    private Product buildProduct() {
        Product product = new Product();
        product.setId(10L);
        product.setName("test");
        product.setPrice(new BigDecimal("100.12"));
        return product;
    }

    @Test
    void testToBytes() throws JsonProcessingException {
        Product product1 = buildProduct();
        byte[] productBytes = JacksonUtil.toBytes(product1);
        Assertions.assertThat(productBytes).isNotEmpty();

        Product product2 = JacksonUtil.parseObject(new String(productBytes, StandardCharsets.UTF_8), Product.class);
        Assertions.assertThat(product1.getId()).isEqualTo(product2.getId());
        Assertions.assertThat(product1.getName()).isEqualTo(product2.getName());
        Assertions.assertThat(product1.getPrice()).isEqualTo(product2.getPrice());
    }

    @Test
    void testParseObject() {
        Product product1 = buildProduct();
        List<Product> products1 = new ArrayList<>();
        products1.add(product1);

        String productsStr = JacksonUtil.toJson(products1);
        Assertions.assertThat(productsStr).isNotBlank();

        List<Product> products2 = JacksonUtil.parseObject(productsStr, new TypeReference<List<Product>>() {
        });
        Assertions.assertThat(products2).isNotEmpty();
        Assertions.assertThat(products2.size()).isEqualTo(products1.size());

        Product product2 = products2.get(0);
        Assertions.assertThat(product1.getId()).isEqualTo(product2.getId());
        Assertions.assertThat(product1.getName()).isEqualTo(product2.getName());
        Assertions.assertThat(product1.getPrice()).isEqualTo(product2.getPrice());
    }

    @Test
    void testParse() {
        Product product1 = buildProduct();
        String productStr = JacksonUtil.toJson(product1);
        Assertions.assertThat(productStr).isNotBlank();

        JsonNode jsonNode = JacksonUtil.parse(productStr);
        Assertions.assertThat(product1.getId()).isEqualTo(jsonNode.get("id").asLong());
        Assertions.assertThat(product1.getName()).isEqualTo(jsonNode.get("name").asText());
        Assertions.assertThat(product1.getPrice()).isEqualTo(jsonNode.get("price").asText());
    }

}