package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.cases;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core.DynamicRestHighLevelClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicElasticsearchTest extends AbstractIntegrationTest {

    @Autowired
    private DynamicRestHighLevelClient dynamicRestHighLevelClient;

    @Test
    void test() {
        Assertions.assertThat(dynamicRestHighLevelClient).isNotNull();
    }

}