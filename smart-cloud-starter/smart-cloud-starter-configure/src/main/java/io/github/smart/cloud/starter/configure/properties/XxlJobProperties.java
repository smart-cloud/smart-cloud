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
package io.github.smart.cloud.starter.configure.properties;

import lombok.Getter;
import lombok.Setter;
import io.github.smart.cloud.common.pojo.Base;

/**
 * xxl-job配置属性
 *
 * @author collin
 * @date 2021-10-31
 */
@Getter
@Setter
public class XxlJobProperties extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * admin端地址
     */
    private String adminAddresses;

    /**
     * job应用名
     */
    private String appName;

    /**
     * admin端ip
     */
    private String ip;

    /**
     * admin端端口
     */
    private Integer port;

    /**
     * 访问admin端的token
     */
    private String accessToken;

    /**
     * 日志保存路径
     */
    private String logPath;

    /**
     * 日志保存天数（默认30天）
     */
    private Integer logRetentionDays = 30;

}