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