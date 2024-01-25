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
package io.github.smart.cloud.starter.monitor.event;

import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 服务状态变更事件
 *
 * @author collin
 * @date 2024-01-25
 */
@Getter
@Setter
@ToString
public abstract class AbstractAppChangeEvent extends ApplicationEvent {

    /**
     * 服务名
     */
    private String name;
    /**
     * 在线实例数
     */
    private Long healthInstanceCount;
    /**
     * 服务地址信息
     */
    private String url;
    private StatusInfo statusInfo;

    public AbstractAppChangeEvent(Object source) {
        super(source);
    }

}