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
package io.github.smart.cloud.starter.mybatis.plus.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义方法配置
 *
 * @author collin
 * @date 2024-03-08
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SmartSqlMethod {

    /**
     * truncate表操作
     */
    TRUNCATE("truncate", "truncate表操作", "TRUNCATE TABLE %s"),
    /**
     * 是否存在（通过select 1 from Xxx where z=? limit 1查询，优化性能）
     */
    IS_EXIST("isExist", "是否存在（通过select 1 from Xxx where zzz=? limit 1查询，优化性能）", "<script>SELECT 1 FROM %s %s LIMIT 1</script>");

    /**
     * 方法名
     */
    private final String method;
    /**
     * 描述
     */
    private final String desc;
    /**
     * sql模板
     */
    private final String sql;

}