package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.support;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.IOException;

public class ElasticsearchMockServer {

    private static EmbeddedElastic embeddedElastic;

    public static void startMockServer(Integer port) throws IOException, InterruptedException {
        embeddedElastic = EmbeddedElastic.builder()
                // https://github.com/elastic/elasticsearch查看版本号
                .withElasticVersion("6.8.23")
                .withSetting(PopularProperties.HTTP_PORT, port)
                .build()
                .start();

    }

    public static void stop() {
        embeddedElastic.stop();
    }

}
