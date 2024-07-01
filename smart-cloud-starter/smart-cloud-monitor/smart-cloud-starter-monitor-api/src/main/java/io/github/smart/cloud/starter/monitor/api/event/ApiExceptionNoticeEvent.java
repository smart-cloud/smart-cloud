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
package io.github.smart.cloud.starter.monitor.api.event;

import io.github.smart.cloud.starter.monitor.api.dto.ApiExceptionDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 接口异常通知事件
 *
 * @author collin
 * @date 2024-07-01
 */
@Getter
public class ApiExceptionNoticeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final List<ApiExceptionDTO> apiExceptions;

    public ApiExceptionNoticeEvent(Object source, List<ApiExceptionDTO> apiExceptions) {
        super(source);
        this.apiExceptions = apiExceptions;
    }

}