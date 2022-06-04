package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.support;

import org.mockserver.integration.ClientAndServer;

public class ElasticsearchMockServer {

    private static ClientAndServer mockServer;

    public static void startMockServer(Integer... port) {
        mockServer = ClientAndServer.startClientAndServer(port);
    }

    public static void stop() {
        mockServer.stop();
    }

}
