package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.support;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.verify.VerificationTimes;

import java.util.HashMap;
import java.util.Map;

public class ElasticsearchMockClient {

    private final MockServerClient client;

    public String version = "2.4";
    public String clusterName = "mock";

    public ElasticsearchMockClient(final MockServerClient client) {
        this.client = client;

        setup();
    }

    public void setup() {

        this.client.when(
                HttpRequest.request()
                        .withPath(".*/_count")
        ).respond(toHttpResponse(new JSONObject(map("count", 0))));

        this.client.when(
                HttpRequest.request()
                        .withPath(".*/_search")
        ).respond(toHttpResponse(new JSONObject(map("took", 10, "hits" , map("total", 0)))));

        this.client.when(
                HttpRequest.request()
                        .withPath(".*/_refresh")
        ).respond(toHttpResponse(new JSONObject(map("_shards", map("total", 100, "successful", 100, "failed", 0)))));

        this.client.when(
                HttpRequest.request()
                        .withPath(".*/_bulk")
        ).respond(toHttpResponse(new JSONObject(map("took", 10, "errors", false, "items", new JSONArray()))));

        // Create index
        this.client.when(
                HttpRequest.request().withMethod("PUT")
        )
                .respond(toHttpResponse(new JSONObject(map("acknowledged", true, "shards_acknowledged", true))));

        // Check if index exists
        this.client.when(
                HttpRequest.request().withMethod("HEAD")
        )
                .respond(HttpResponse.notFoundResponse());

        // Default
        this.client.when(
                HttpRequest.request()
        ).respond(toHttpResponse(new JSONObject(map("name", "mock", "cluster_name", clusterName, "version", map("number", version)))));

    }

    public void assertIndexCreated(final String index) throws AssertionError {
        this.client.verify(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/" + index),
                VerificationTimes.once());
    }

    private HttpResponse toHttpResponse(final JSONObject data) {
        return HttpResponse.response(data.toString()).withHeader("Content-Type", "application/json");
    }

    private <K, V> Map<K, V> map(Object... args) {
        Map<K, V> res = new HashMap<>();
        K key = null;
        for (Object arg : args) {
            if (key == null) {
                key = (K) arg;
            } else {
                res.put(key, (V) arg);
                key = null;
            }
        }
        return res;
    }

}