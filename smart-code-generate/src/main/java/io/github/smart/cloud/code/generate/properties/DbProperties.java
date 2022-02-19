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
package io.github.smart.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author collin
 * @desc 数据库连接配置
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class DbProperties {
    /**
     * 数据库连接url（必填）
     */
    private String url;
    /**
     * 数据库用户名（必填）
     */
    private String username;
    /**
     * 用户库密码（必填）
     */
    private String password;
    /**
     * 数据库驱动（默认mysql驱动）
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    /**
     * sql脚本文件全路径
     */
    private String schema;

}