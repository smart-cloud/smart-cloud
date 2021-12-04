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
package org.smartframework.cloud.code.generate.properties;

import lombok.Setter;
import lombok.ToString;

/**
 * @author liyulin
 * @desc 工程路径配置
 * @date 2019/11/08
 */
@Setter
@ToString
public class PathProperties {

    /**
     * rpc工程路径
     */
    private String rpc;
    /**
     * 服务工程路径
     */
    private String service;

    public String getRpc() {
        if (rpc != null && rpc.trim().length() > 0) {
            return rpc;
        }
        if (isWindowSystem()) {
            return "c:/codegenerate/rpc-module/";
        }
        return "/tmp/codegenerate/rpc-module/";
    }

    public String getService() {
        if (service != null && service.trim().length() > 0) {
            return service;
        }
        if (isWindowSystem()) {
            return "c:/codegenerate/service-module/";
        }
        return "/tmp/codegenerate/service-module/";
    }

    private boolean isWindowSystem() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
    }

}