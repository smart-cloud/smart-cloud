package org.smartframework.cloud.starter.mybatis.common.mapper.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表字段f_sys_del_state对应的枚举值
 *
 * @author liyulin
 * @date 2019-03-24
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DelStateEnum {

	/** 正常 */
	NORMAL((byte)1),
	/** 已删除 */
	DELETED((byte)2);

	/** 记录状态 */
	private Byte delState;

}