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

/**
 * 每张表默认的字段
 *
 * @author collin
 * @date 2019-07-13
 */
public enum DefaultColumnEnum {

	/** 主键id */
	f_id,
	/** 新增时间 */
	f_sys_insert_time,
	/** 修改时间 */
	f_sys_upd_time,
	/** 删除时间 */
	f_sys_del_time,
	/** 新增人 */
	f_sys_insert_user,
	/** 修改人 */
	f_sys_upd_user,
	/** 删除人 */
	f_sys_del_user,
	/** 删除状态 */
	f_sys_del_state;

	public static boolean contains(String name) {
		DefaultColumnEnum[] defaultColumns = DefaultColumnEnum.values();
		for (DefaultColumnEnum defaultColumn : defaultColumns) {
			if (defaultColumn.toString().equals(name)) {
				return true;
			}
		}
		return false;
	}

}