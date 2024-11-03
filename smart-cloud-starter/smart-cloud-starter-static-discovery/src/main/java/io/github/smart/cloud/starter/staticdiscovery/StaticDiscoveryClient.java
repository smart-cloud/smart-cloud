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
package io.github.smart.cloud.starter.staticdiscovery;

import io.github.smart.cloud.starter.staticdiscovery.properties.StaticDiscoveryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 静态地址发现
 *
 * @author collin
 * @date 2024-11-03
 * @see SimpleDiscoveryClientAutoConfiguration
 */
@RequiredArgsConstructor
public class StaticDiscoveryClient implements DiscoveryClient, InitializingBean {

    private final StaticDiscoveryProperties staticDiscoveryProperties;
    private Map<String, List<ServiceInstance>> serviceInstanceRoute;
    private List<String> services;

    @Override
    public String description() {
        return "Static Discovery Client";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return serviceInstanceRoute.get(serviceId);
    }

    @Override
    public List<String> getServices() {
        return services;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, List<String>> serviceInstanceConfig = staticDiscoveryProperties.getInstanceConfig();
        if (serviceInstanceConfig == null || serviceInstanceConfig.isEmpty()) {
            this.serviceInstanceRoute = new HashMap<>(0);
            this.services = new ArrayList<>(0);
            return;
        }

        Map<String, List<ServiceInstance>> tempServiceInstanceRoute = new HashMap<>(0);
        List<String> tempServices = new ArrayList<>(0);
        serviceInstanceConfig.forEach((serviceId, uris) -> {
            AtomicInteger counter = new AtomicInteger(1);
            List<ServiceInstance> serviceInstances = uris.stream().map(item -> {
                URI uri = URI.create(item);
                return new DefaultServiceInstance(String.format("%s-%d", serviceId, counter.getAndIncrement()), serviceId, uri.getHost(), uri.getPort(), false);
            }).collect(Collectors.toList());

            tempServices.add(serviceId);
            tempServiceInstanceRoute.put(serviceId, serviceInstances);
        });

        this.serviceInstanceRoute = tempServiceInstanceRoute;
        this.services = tempServices;
    }

}