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
package io.github.smart.cloud.starter.mybatis.plus.util;

import com.baomidou.mybatisplus.core.toolkit.Sequence;

/**
 * 分布式id生成器
 *
 * @author collin
 * @date 2022-03-06
 */
public class IdWorker {

    private static final IdWorker INSTANCE = new IdWorker();
    private Sequence sequence;

    private IdWorker() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static IdWorker getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化workerId、dataCenterId
     *
     * @param workerId
     * @param dataCenterId
     */
    public void init(long workerId, long dataCenterId) {
        sequence = new Sequence(workerId, dataCenterId);
    }

    /**
     * 获取唯一的id值
     *
     * @return
     */
    public Long nextId() {
        if (sequence == null) {
            throw new NullPointerException("IdWorker[workId,dataCenterId] is not initedid!");
        }

        return sequence.nextId();
    }

}