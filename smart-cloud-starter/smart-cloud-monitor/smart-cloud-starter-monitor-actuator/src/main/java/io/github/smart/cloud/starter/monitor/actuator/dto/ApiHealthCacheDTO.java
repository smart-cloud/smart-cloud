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
package io.github.smart.cloud.starter.monitor.actuator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.LongAdder;

/**
 * 接口访问状态（成功、失败）缓存信息
 *
 * @author collin
 * @date 2024-01-6
 */
@Getter
@ToString
@AllArgsConstructor
public class ApiHealthCacheDTO {

    /**
     * 成功数
     */
    private volatile LongAdder successCount;
    /**
     * 失败数
     */
    private volatile LongAdder failCount;
    /**
     * 异常
     */
    @Setter
    private Throwable throwable;

}