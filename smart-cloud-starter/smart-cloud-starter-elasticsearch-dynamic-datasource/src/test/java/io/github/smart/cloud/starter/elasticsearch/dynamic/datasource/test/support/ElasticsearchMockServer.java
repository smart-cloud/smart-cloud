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
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.support;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ElasticsearchMockServer {

    private static EmbeddedElastic embeddedElastic;

    public static EmbeddedElastic startMockServer(int httpPort, int tcpPort, String installationDirectory) throws IOException, InterruptedException {
        ElasticsearchMockServer.embeddedElastic = EmbeddedElastic.builder()
                // https://github.com/elastic/elasticsearch查看版本号
                .withElasticVersion("6.8.23")
                .withSetting(PopularProperties.HTTP_PORT, httpPort)
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, tcpPort)
                .withInstallationDirectory(new File(installationDirectory))
                .withEsJavaOpts("-Xms128m -Xmx512m")
                .withStartTimeout(2, TimeUnit.MINUTES)
                .build()
                .start();
        return embeddedElastic;
    }

    public static void stop() {
        if (embeddedElastic != null) {
            embeddedElastic.stop();
        }
    }

}
