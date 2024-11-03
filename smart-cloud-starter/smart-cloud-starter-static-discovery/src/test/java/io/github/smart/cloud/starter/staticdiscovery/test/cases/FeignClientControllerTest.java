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
package io.github.smart.cloud.starter.staticdiscovery.test.cases;

import feign.RetryableException;
import io.github.smart.cloud.starter.staticdiscovery.StaticDiscoveryClient;
import io.github.smart.cloud.starter.staticdiscovery.properties.StaticDiscoveryProperties;
import io.github.smart.cloud.starter.staticdiscovery.test.prepare.Application;
import io.github.smart.cloud.starter.staticdiscovery.test.prepare.rpc.FeignClientRpc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FeignClientControllerTest {

    @Autowired
    private FeignClientRpc feignClientRpc;
    @Autowired
    private RefreshScope refreshScope;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @Order(1)
    void testBeforeRefreshConfig() {
        String requestStr = "test feign";
        String response = feignClientRpc.get(requestStr);
        Assertions.assertThat(response).isEqualTo(requestStr);
    }

    @Test
    @Order(2)
    void testRefreshConfig() throws InterruptedException {
        String serviceId = "feignClientRpc";
        // 刷新配置前实例
        CompositeDiscoveryClient oldCompositeDiscoveryClient = applicationContext.getBean(CompositeDiscoveryClient.class);
        List<ServiceInstance> oldInstances = oldCompositeDiscoveryClient.getDiscoveryClients().get(0).getInstances(serviceId);
        Assertions.assertThat(oldInstances)
                .isNotEmpty()
                .hasSize(2);

        // 刷新配置
        StaticDiscoveryProperties staticDiscoveryProperties = applicationContext.getBean(StaticDiscoveryProperties.class);
        Map<String, List<String>> serviceInstances = staticDiscoveryProperties.getInstanceConfig();
        serviceInstances.put("feignClientRpc", Arrays.asList("http://192.168.100.107:10000"));
        refreshScope.refresh(StaticDiscoveryClient.class);
        // 等待服务列表缓存失效
        TimeUnit.MILLISECONDS.sleep(1100);

        // 刷新配置后实例
        CompositeDiscoveryClient newCompositeDiscoveryClient = applicationContext.getBean(CompositeDiscoveryClient.class);
        List<ServiceInstance> newInstances = newCompositeDiscoveryClient.getDiscoveryClients().get(0).getInstances(serviceId);
        Assertions.assertThat(newInstances)
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThatThrownBy(() -> {
                    String requestStr = "test feign";
                    feignClientRpc.get(requestStr);
                })
                .isInstanceOf(RetryableException.class)
                .hasMessageContaining("connect timed out executing");
    }

}