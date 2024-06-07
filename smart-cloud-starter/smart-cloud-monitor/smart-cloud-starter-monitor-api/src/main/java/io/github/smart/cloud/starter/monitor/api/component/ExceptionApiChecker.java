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
package io.github.smart.cloud.starter.monitor.api.component;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.smart.cloud.exception.ConfigException;
import io.github.smart.cloud.monitor.common.dto.wework.WeworkRobotMarkdownMessageDTO;
import io.github.smart.cloud.starter.monitor.api.dto.ApiExceptionDTO;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.utility.HttpUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;
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

    private final ApiMonitorProperties apiMonitorProperties;
    private final ApiMonitorRepository apiRepository;
    private Environment environment;
    private HttpHost proxy;
    private ScheduledExecutorService exceptionApiCheckSchedule;
    private String weworkRobotUrl;
    private String ip;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(apiMonitorProperties.getProxyHost())) {
            proxy = new HttpHost(apiMonitorProperties.getProxyHost(), apiMonitorProperties.getPort());
        }

        if (StringUtils.isBlank(apiMonitorProperties.getRobotKey())) {
            throw new ConfigException("The robot key is not configured");
        }

        this.weworkRobotUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s", apiMonitorProperties.getRobotKey());
        this.ip = InetAddress.getLocalHost().getHostAddress();

        exceptionApiCheckSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("exception-api-notice-schedule"));
        exceptionApiCheckSchedule.scheduleWithFixedDelay(this::checkExceptionApiAndNotice, apiMonitorProperties.getApiExceptionNoticeIntervalSeconds(),
                apiMonitorProperties.getApiExceptionNoticeIntervalSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 检测异常接口，并发送通知
     */
    public boolean checkExceptionApiAndNotice() {
        List<ApiExceptionDTO> apiExceptions = apiRepository.getApiExceptions();
        if (apiExceptions.isEmpty()) {
            return true;
        }

        String request = buildWeworkRobotMessage(apiExceptions);
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
     * @param apiExceptions
     * @return
     */
    private String buildWeworkRobotMessage(List<ApiExceptionDTO> apiExceptions) {
        StringBuilder content = new StringBuilder(128);
        content.append("**").append(environment.getProperty("spring.application.name")).append("** ")
                .append(TimeUnit.SECONDS.toMinutes(apiMonitorProperties.getCleanIntervalSeconds()))
                .append("分钟异常接口统计:")
                .append("\n**IP**：").append(ip);
        boolean needMention = false;
        int apiIndex = 0;
        for (ApiExceptionDTO apiException : apiExceptions) {
            content.append("\n\n>**接口").append(++apiIndex).append("**：").append(apiException.getName());
            content.append("\n>**请求总数**：").append(apiException.getTotal());
            content.append("\n>**失败数**：").append(apiException.getFailCount());
            content.append("\n>**失败率**：<font color=\"warning\">").append(apiException.getFailRate()).append("</font >");
            String message = apiException.getMessage();
            if (message != null) {
                content.append("\n>**异常信息：<font color=\"warning\">").append(message).append("</font >**");
                needMention = matchMention(needMention, message);
            }
        }

        if (needMention && CollectionUtils.isNotEmpty(apiMonitorProperties.getMentionedList())) {
            content.append("\n\n<@").append(StringUtils.join(apiMonitorProperties.getMentionedList(), ">\n<@")).append(">");
        }

        return JacksonUtil.toJson(new WeworkRobotMarkdownMessageDTO(content.toString()));
    }

    /**
     * 匹配是否需要提醒
     *
     * @param needMention
     * @param exceptionMessage
     * @return
     */
    private boolean matchMention(boolean needMention, String exceptionMessage) {
        if (needMention) {
            return true;
        }

        Set<String> needMentionedExceptionClassNames = apiMonitorProperties.getNeedMentionedExceptionClassNames();
        for (String needMentionedExceptionClassName : needMentionedExceptionClassNames) {
            if (exceptionMessage.contains(needMentionedExceptionClassName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() throws Exception {
        if (exceptionApiCheckSchedule != null) {
            exceptionApiCheckSchedule.shutdown();
        }
    }

}