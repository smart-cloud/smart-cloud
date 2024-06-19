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

/**
 * 数据库查询监控配置属性
 *
 * @author collin
 * @date 2024-06-19
 */
@Getter
@Setter
@ToString
public class DbQueryMonitorProperties {

    /**
     * 数据库查询监控最小值
     */
    private int queryMinSize = 1;
    /**
     * 是否清除查询统计
     */
    private boolean cleanQueryStatistics = true;
    /**
     * 打印最大数
     */
    private int printMaxSize = 5;

}