package org.smartframework.cloud.code.generate.enums;

/**
 * 每张表默认的字段
 *
 * @author liyulin
 * @date 2019-07-13
 */
public enum DefaultColumnEnum {
	
	f_id,
	f_sys_add_time,
	f_sys_upd_time,
	f_sys_del_time,
	f_sys_add_user,
	f_sys_upd_user,
	f_sys_del_user,
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