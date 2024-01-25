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
package io.github.smart.cloud.starter.monitor.component;

import io.github.smart.cloud.starter.monitor.properties.MonitorProperties;
import io.github.smart.cloud.starter.monitor.properties.ProxyProperties;
import io.github.smart.cloud.starter.monitor.properties.ServiceInfoProperties;
import io.github.smart.cloud.utility.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * 企业微信机器人发送消息
 *
 * @author collin
 * @date 2024-01-18
 */
@Slf4j
@RequiredArgsConstructor
public class RobotComponent implements SmartInitializingSingleton {

    private final MonitorProperties monitorProperties;
    private HttpHost proxy = null;
    /**
     * 机器人地址
     */
    private String robotUrlTemplate = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";
    /**
     * 企业微信机器人通知模板
     */
    private String rabotMessageTemplate = "{\"msgtype\":\"markdown\",\"markdown\":{\"content\":\"%s\"}}";

    /**
     * 发送企业微信通知
     *
     * @param robotKey
     * @param content
     */
    public void sendWxworkNotice(String robotKey, String content) {
        String url = String.format(robotUrlTemplate, robotKey);
        try {
            HttpUtil.postWithRaw(url, String.format(rabotMessageTemplate, content), proxy);
        } catch (Exception e) {
            log.error("send WXWork notice fail|url={}, content={}", url, content, e);
        }
    }

    /**
     * 获取机器人key
     *
     * @param serviceName
     * @return
     */
    public String getRobotKey(String serviceName) {
        ServiceInfoProperties serviceInfoProperties = monitorProperties.getServiceInfos().get(serviceName);
        if (serviceInfoProperties == null) {
            return monitorProperties.getRobotKey();
        }

        return serviceInfoProperties.getRobotKey();
    }

    @Override
    public void afterSingletonsInstantiated() {
        ProxyProperties proxyProperties = monitorProperties.getProxy();
        if (existProxy(proxyProperties)) {
            this.proxy = new HttpHost(proxyProperties.getHost(), proxyProperties.getPort());
        }
    }

    /**
     * 是否存在代理代理
     *
     * @param proxyProperties
     * @return
     */
    private boolean existProxy(ProxyProperties proxyProperties) {
        return proxyProperties != null
                && (proxyProperties.getHost() != null && proxyProperties.getHost().trim().length() > 0)
                && (proxyProperties.getPort() != null && proxyProperties.getPort() > 0);
    }

}