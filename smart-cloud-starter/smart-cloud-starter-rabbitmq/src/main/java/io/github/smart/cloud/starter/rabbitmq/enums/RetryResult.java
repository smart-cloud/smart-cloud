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
package io.github.smart.cloud.starter.rabbitmq.enums;

/**
 * 重试结果
 *
 * @author collin
 * @date 2022-03-14
 */
public enum RetryResult {

    /**
     * 不支持重试
     */
    NOT_SUPPORT,
    /**
     * 已达到重试最大阈值
     */
    REACHED_RETRY_THRESHOLD,
    /**
     * 重试成功
     */
    SUCCESS;

}