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
package io.github.smart.cloud.starter.monitor.admin.test.cases;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.Endpoints;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import io.github.smart.cloud.starter.monitor.admin.component.metrics.IInstanceMetricsMonitorComponent;
import io.github.smart.cloud.starter.monitor.admin.test.prepare.App;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, args = "--spring.profiles.active=metrics-fail", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MetricsFailTest {

    @Autowired
    private List<IInstanceMetricsMonitorComponent> instanceMetricsMonitorComponents;
    @Autowired
    private InstanceRegistry instanceRegistry;

    @Test
    void testMetrics() throws InterruptedException {
        Assertions.assertThat(instanceMetricsMonitorComponents).isNotEmpty();

        TimeUnit.SECONDS.sleep(15);
        System.gc();
        TimeUnit.SECONDS.sleep(15);

        List<Instance> instances = instanceRegistry.getInstances()
                .filter(Instance::isRegistered)
                .filter(instance -> {
                    Endpoints endpoints = instance.getEndpoints();
                    return endpoints != null && endpoints.isPresent("metrics");
                }).collectList().block(Duration.ofMillis(10_000L));

        Assertions.assertThat(instances).isNotEmpty();
        for (Instance instance : instances) {
            instanceMetricsMonitorComponents.forEach(instanceMetricsMonitorComponent -> {
                try {
                    boolean result = instanceMetricsMonitorComponent.alert(instance);
                    if (!result) {
                        log.warn("------>{}", instanceMetricsMonitorComponent.getInstanceMetric().name());
                    }
                    Assertions.assertThat(result).isEqualTo(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}