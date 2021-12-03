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
package org.smartframework.cloud.utility.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.message.BasicNameValuePair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.utility.HttpUtil;
import org.smartframework.cloud.utility.test.integration.prepare.TestApplication;
import org.smartframework.cloud.utility.test.integration.prepare.vo.GetPageReqVO;
import org.smartframework.cloud.utility.test.integration.prepare.vo.GetPageRespVO;
import org.smartframework.cloud.utility.test.integration.prepare.vo.PostUrlEncodedRespVO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HttpUtilIntegrationTest {

    /**
     * 服务启动端口
     */
    private static final int DEFAULT_PORT = 8080;
    private static final String REQUEST_URL_PREFIX = "http://localhost:" + DEFAULT_PORT + "/test";

    @Test
    void testGet() throws IOException {
        String result = HttpUtil.get(REQUEST_URL_PREFIX + "?str=test", null);
        Assertions.assertThat(result).isEqualTo("test");
    }

    @Test
    void testGetReturnType() throws IOException {
        GetPageReqVO reqVO = new GetPageReqVO();
        reqVO.setStr("test");
        reqVO.setIds(new long[]{1, 2, 3});
        GetPageRespVO result = HttpUtil.get(REQUEST_URL_PREFIX + "/page", reqVO, new TypeReference<GetPageRespVO>() {
        });
        Assertions.assertThat(result.getStr()).isEqualTo("test");
        Assertions.assertThat(result.getIds()).isNotEmpty();
        Assertions.assertThat(result.getIds().length).isEqualTo(3);
        Assertions.assertThat(result.getIds()[2]).isEqualTo(3);
    }

    @Test
    void testPostWithRaw() throws IOException {
        String result = HttpUtil.postWithRaw(REQUEST_URL_PREFIX, "test");
        Assertions.assertThat(result).isEqualTo("test");
    }

    @Test
    void testPostWithRawReturnType() throws IOException {
        List<String> result = HttpUtil.postWithRaw(REQUEST_URL_PREFIX + "/list", "test",
                new TypeReference<List<String>>() {
                });
        Assertions.assertThat(result.get(0)).isEqualTo("test");
    }

    @Test
    void testPostWithUrlEncoded() throws IOException {
        List<BasicNameValuePair> parameters = new ArrayList<>();
        String id = "100";
        parameters.add(new BasicNameValuePair("id", id));
        PostUrlEncodedRespVO result = HttpUtil.postWithUrlEncoded(REQUEST_URL_PREFIX + "/postUrlEncoded", parameters,
                new TypeReference<PostUrlEncodedRespVO>() {
                });
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
    }

}