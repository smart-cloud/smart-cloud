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
package io.github.smart.cloud.starter.monitor.admin.event.notice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 服务实例数检查通知事件
 *
 * @author collin
 * @date 2024-02-23
 */
@Getter
@Setter
@ToString
public class ServiceNodeCountCheckNoticeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /**
     * 服务名
     */
    private final String name;
    /**
     * 当前在线实例数
     */
    private final int nodeCount;

    public ServiceNodeCountCheckNoticeEvent(Object source, String name, int nodeCount) {
        super(source);
        this.name = name;
        this.nodeCount = nodeCount;
    }

}