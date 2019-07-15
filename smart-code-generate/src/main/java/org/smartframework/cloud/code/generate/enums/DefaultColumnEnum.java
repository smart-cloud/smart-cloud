package org.smartframework.cloud.code.generate.enums;

/**
 * 每张表默认的字段
 *
 * @author liyulin
 * @date 2019-07-13
 */
public enum DefaultColumnEnum {

	/** 主键id */
	f_id,
	/** 新增时间 */
	f_sys_add_time,
	/** 修改时间 */
	f_sys_upd_time,
	/** 删除时间 */
	f_sys_del_time,
	/** 新增人 */
	f_sys_add_user,
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