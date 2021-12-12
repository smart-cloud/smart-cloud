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
package org.smartframework.cloud.code.generate.constants;

/**
 * 数据库相关常量
 *
 * @author collin
 * @date 2021-12-12
 */
public interface DbConstants {

    /**
     * jdbc查询表的类型
     */
    String TABLE_TYPE = "TABLE";

    /**
     * 数据库连接属性
     *
     * @author collin
     * @date 2021-12-12
     */
    interface ConnectionProperties {
        /**
         * 数据库用户名
         */
        String USER = "user";
        /**
         * 数据库密码
         */
        String PASSWORD = "password";
        /**
         * 获取Oracle元数据 REMARKS信息
         */
        String REMARKS_REPORTING = "remarksReporting";
        /**
         * 获取MySQL元数据 REMARKS信息
         */
        String USE_INFORMATION_SCHEMA = "useInformationSchema";
    }

}