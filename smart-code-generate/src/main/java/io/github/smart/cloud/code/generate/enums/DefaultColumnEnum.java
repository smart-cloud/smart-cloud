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
package io.github.smart.cloud.code.generate.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 每张表默认的字段
 *
 * @author collin
 * @date 2019-07-13
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DefaultColumnEnum {

    /**
     * 主键id
     */
    ID("f_id"),
    /**
     * 新增时间
     */
    SYS_INSERT_TIME("f_sys_insert_time"),
    /**
     * 修改时间
     */
    SYS_UPD_TIME("f_sys_upd_time"),
    /**
     * 删除时间
     */
    SYS_DEL_TIME("f_sys_del_time"),
    /**
     * 新增人
     */
    SYS_INSERT_USER("f_sys_insert_user"),
    /**
     * 修改人
     */
    SYS_UPD_USER("f_sys_upd_user"),
    /**
     * 删除人
     */
    SYS_DEL_USER("f_sys_del_user"),
    /**
     * 删除状态
     */
    SYS_DEL_STATE("f_sys_del_state");

    /**
     * 字段名称
     */
    private String column;

    public static boolean contains(String name) {
        DefaultColumnEnum[] defaultColumns = DefaultColumnEnum.values();
        for (DefaultColumnEnum defaultColumn : defaultColumns) {
            if (defaultColumn.getColumn().equals(name)) {
                return true;
            }
        }
        return false;
    }

}