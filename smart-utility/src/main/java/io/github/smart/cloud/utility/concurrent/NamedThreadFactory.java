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
package io.github.smart.cloud.utility.concurrent;

import io.github.smart.cloud.constants.SymbolConstant;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 *
 * @author collin
 * @date 2024-02-21
 */
public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger sequence = new AtomicInteger(1);
    private final String prefix;

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        int seq = sequence.getAndIncrement();
        if (prefix.endsWith(SymbolConstant.HYPHEN)) {
            thread.setName(prefix + seq);
        } else {
            thread.setName(prefix + SymbolConstant.HYPHEN + seq);
        }
        thread.setDaemon(true);
        return thread;
    }

}