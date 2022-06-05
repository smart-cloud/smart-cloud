package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.cases;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core.DynamicRestHighLevelClient;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.prepare.constants.EsDatasources;
import org.assertj.core.api.Assertions;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class DynamicElasticsearchTest extends AbstractIntegrationTest {

    @Autowired
    private DynamicRestHighLevelClient dynamicRestHighLevelClient;

    @Test
    void test() {
        boolean tag = existIndex("risk_product");
        System.out.println(tag);
    }

    public CreateIndexResponse createIndex(String indexName) throws IOException {
        CreateIndexResponse response;
        if(existIndex(indexName)){
            response = new CreateIndexResponse(false, false, indexName);
            return response;
        }

        CreateIndexRequest request = new CreateIndexRequest(indexName);
        response = dynamicRestHighLevelClient.getRestHighLevelClient(EsDatasources.PRODUCT).indices().create(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 判断索引是否存在
     * @param indexName 要判断是否存在的索引的名字，传入如： testindex
     * @return 返回是否存在。
     * 		<ul>
     * 			<li>true:存在</li>
     * 			<li>false:不存在</li>
     * 		</ul>
     */
    public boolean existIndex(String indexName){
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        boolean exists;
        try {
            exists = dynamicRestHighLevelClient.getRestHighLevelClient(EsDatasources.PRODUCT).indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return exists;
    }

}