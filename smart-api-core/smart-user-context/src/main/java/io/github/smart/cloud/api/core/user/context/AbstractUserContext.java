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
package io.github.smart.cloud.api.core.user.context;

/**
 * 用户上下文
 *
 * @author collin
 * @date 2021-03-03
 */
public abstract class AbstractUserContext {

    /**
     * 用户上下文信息
     */
    protected static final ThreadLocal<SmartUser> USER_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 设置用户上下文信息
     *
     * @param smartUser
     */
    public static void setContext(SmartUser smartUser) {
        USER_THREAD_LOCAL.set(smartUser);
    }

    /**
     * 移除用户上下文信息
     */
    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

}