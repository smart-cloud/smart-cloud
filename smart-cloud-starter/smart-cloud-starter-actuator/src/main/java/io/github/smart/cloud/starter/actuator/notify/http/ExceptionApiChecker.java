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
package io.github.smart.cloud.starter.actuator.notify.http;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.exception.ConfigException;
import io.github.smart.cloud.starter.actuator.dto.UnHealthApiDTO;
import io.github.smart.cloud.starter.actuator.properties.HealthProperties;
import io.github.smart.cloud.starter.actuator.repository.ApiHealthRepository;
import io.github.smart.cloud.utility.HttpUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异常接口监控，企业微信机器人通知
 *
 * @author collin
 * @date 2024-04-28
 */
@Slf4j
@RequiredArgsConstructor
public class ExceptionApiChecker implements EnvironmentAware, InitializingBean, DisposableBean {

    private final HealthProperties healthProperties;
    private final ApiHealthRepository apiRepository;
    private Environment environment;
    private HttpHost proxy;
    private ScheduledExecutorService exceptionApiCheckSchedule;
    private String weworkRobotUrl;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(healthProperties.getProxyHost())) {
            proxy = new HttpHost(healthProperties.getProxyHost(), healthProperties.getPort());
        }

        if (StringUtils.isBlank(healthProperties.getRobotKey())) {
            throw new ConfigException("The robot key is not configured");
        }

        this.weworkRobotUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s", healthProperties.getRobotKey());

        exceptionApiCheckSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("exception-api-notice-schedule"));
        exceptionApiCheckSchedule.scheduleWithFixedDelay(this::checkExceptionApiAndNotice, healthProperties.getApiExceptionNoticeIntervalSeconds(),
                healthProperties.getApiExceptionNoticeIntervalSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 检测异常接口，并发送通知
     */
    public boolean checkExceptionApiAndNotice() {
        List<UnHealthApiDTO> unHealthInfos = apiRepository.getUnHealthInfos();
        if (unHealthInfos.isEmpty()) {
            return true;
        }

        String request = buildWeworkRobotMessage(unHealthInfos);
        try {
            String result = HttpUtil.postWithRaw(weworkRobotUrl, request, proxy);
            // {"errcode":0,"errmsg":"ok"}
            if (StringUtils.isBlank(result)) {
                return false;
            }

            JsonNode resultNode = JacksonUtil.parse(result);
            if (resultNode == null) {
                return false;
            }

            JsonNode codeNode = resultNode.get("errcode");
            return codeNode != null && codeNode.asInt() == 0;
        } catch (IOException e) {
            log.error("send http request fail|request={}", request, e);
            return false;
        }
    }

    /**
     * 构造企业微信机器人消息
     *
     * @param unHealthInfos
     * @return
     */
    private String buildWeworkRobotMessage(List<UnHealthApiDTO> unHealthInfos) {
        StringBuilder content = new StringBuilder(32);
        content.append("**").append(environment.getProperty("spring.application.name")).append("** ")
                .append(TimeUnit.SECONDS.toMinutes(healthProperties.getCleanIntervalSeconds()))
                .append("分钟异常接口统计:");
        int apiIndex = 0;
        for (UnHealthApiDTO unHealthInfo : unHealthInfos) {
            content.append("\n\n>**接口").append(++apiIndex).append("**：").append(unHealthInfo.getName());
            content.append("\n>**请求总数**：").append(unHealthInfo.getTotal());
            content.append("\n>**失败数**：").append(unHealthInfo.getFailCount());
            content.append("\n>**失败率**：<font color=\\\"warning\\\">").append(unHealthInfo.getFailRate()).append("</font >");
            String failMessage = unHealthInfo.getFailMessage();
            if (failMessage != null) {
                if (failMessage.contains(SymbolConstant.DOUBLE_QUOTE)) {
                    failMessage = StringUtils.remove(failMessage, SymbolConstant.DOUBLE_QUOTE);
                }
                content.append("\n>**异常信息**：").append(failMessage);
            }
        }

        return String.format("{\"msgtype\":\"markdown\",\"markdown\":{\"content\":\"%s\"}}", content);
    }

    @Override
    public void destroy() throws Exception {
        if (exceptionApiCheckSchedule != null) {
            exceptionApiCheckSchedule.shutdown();
        }
    }

}