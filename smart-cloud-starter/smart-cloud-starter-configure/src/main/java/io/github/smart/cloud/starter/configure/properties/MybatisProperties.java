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
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * api日志切面配置
 *
 * @author collin
 * @date 2019-06-19
 */
@Getter
@Setter
@ToString
public class MybatisProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * mybatis日志开关 （默认true）
     */
    private boolean enable = true;
    /**
     * 切面日志打印最大长度
     */
    private Integer logMaxLength;
    /**
     * 慢sql时间（单位：毫秒，默认1000毫秒）。耗时达到该值的sql以warn级别打印
     */
    private int slowSqlMinCost = 1000;
    /**
     * 是否只打印慢sql（默认false）。为true时，未达到slowSqlMinCost的sql不打印
     */
    private boolean onlySlowSql = false;
    /**
     * 是否打印sql执行结果（默认true）
     */
    private boolean logResult = true;

    /**
     * 加解密密钥信息<加解密字段类全类名, 加解密秘钥>
     */
    private Map<String, String> cryptKeys = new LinkedHashMap<>();

}