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
package io.github.smart.cloud.monitor.common.dto.wework;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.smart.cloud.monitor.common.enums.WeworkRobotMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * 企业微信机器人text类型消息
 *
 * @author collin
 * @date 2024-05-20
 */
@Getter
@Setter
@ToString
public class WeworkRobotTextMessageDTO extends AbstractWeworkRobotMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息内容
     */
    private WeworkRobotMessageContentDTO text;
    /**
     * 提醒人
     */
    @JsonProperty("mentioned_list")
    private LinkedHashSet<String> mentionedList;

    public WeworkRobotTextMessageDTO(String content) {
        this(content, null);
    }

    public WeworkRobotTextMessageDTO(String content, LinkedHashSet<String> mentionedList) {
        this.text = new WeworkRobotMessageContentDTO(content);
        this.mentionedList = mentionedList;
        setMsgtype(WeworkRobotMessageType.TEXT.getValue());
    }

}