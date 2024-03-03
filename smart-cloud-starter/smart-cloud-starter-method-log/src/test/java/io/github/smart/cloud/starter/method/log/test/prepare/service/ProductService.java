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
package io.github.smart.cloud.starter.method.log.test.prepare.service;

import io.github.smart.cloud.constants.LogLevel;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.method.log.annotation.MethodLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private SmartProperties smartProperties;

    @MethodLog(level = LogLevel.WARN)
    public String query(Long id) {
        return String.valueOf(id);
    }

    /**
     * 慢接口
     *
     * @param id
     * @return
     * @throws InterruptedException
     */
    @MethodLog
    public String queryWithSlow(Long id) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(smartProperties.getMethodLog().getSlowApiMinCost());
        return String.valueOf(id);
    }

}