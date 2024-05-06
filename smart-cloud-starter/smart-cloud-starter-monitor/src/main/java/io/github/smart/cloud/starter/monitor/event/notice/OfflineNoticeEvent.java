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
package io.github.smart.cloud.starter.monitor.event.notice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 服务不在线事件（在线实例为0）
 *
 * @author collin
 * @date 2024-02-23
 */
@Getter
@Setter
@ToString
public class OfflineNoticeEvent extends ApplicationEvent {

    /**
     * 服务名
     */
    private String name;

    public OfflineNoticeEvent(Object source, String name) {
        super(source);
        this.name = name;
    }

}