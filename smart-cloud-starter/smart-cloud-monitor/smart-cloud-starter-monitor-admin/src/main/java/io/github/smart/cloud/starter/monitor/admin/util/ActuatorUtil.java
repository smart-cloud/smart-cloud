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
package io.github.smart.cloud.starter.monitor.admin.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import io.github.smart.cloud.utility.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * actuator工具类
 *
 * @author collin
 * @date 2024-09-18
 */
@Slf4j
public class ActuatorUtil {

    /**
     * 发送get请求，通过actuator接口获取指标数据
     *
     * @param instance
     * @param metricName 指标名
     * @return
     * @throws IOException
     */
    public static String sendGetRequest(Instance instance, String metricName) throws IOException {
        String url = String.format("%s/metrics/%s", instance.getRegistration().getManagementUrl(), metricName);

        return HttpUtil.get(url, null, null);
    }

    /**
     * 解析节点值
     *
     * @param measurementsNodes
     * @param statisticName
     * @return
     */
    public static JsonNode parseValueNode(JsonNode measurementsNodes, String statisticName) {
        for (int i = 0; i < measurementsNodes.size(); i++) {
            JsonNode measurementsNode = measurementsNodes.get(i);
            if (statisticName.equals(measurementsNode.get("statistic").asText())) {
                return measurementsNode.get("value");
            }
        }
        return null;
    }

}