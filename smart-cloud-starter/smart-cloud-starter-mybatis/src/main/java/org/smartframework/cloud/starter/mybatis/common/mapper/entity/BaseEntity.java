package org.smartframework.cloud.starter.mybatis.common.mapper.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.smartframework.cloud.common.pojo.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseEntity extends BaseDto {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "f_id")
	private Long id;

	/** 创建时间 */
	@Column(name = "f_sys_add_time")
	private Date addTime;

	/** 更新时间 */
	@Column(name = "f_sys_upd_time")
	private Date updTime;

	/** 删除时间 */
	@Column(name = "f_sys_del_time")
	private Date delTime;

	/** 新增者 */
	@Column(name = "f_sys_add_user")
	private Long addUser;

	/** 更新者 */
	@Column(name = "f_sys_upd_user")
	private Long updUser;

	/** 删除者 */
	@Column(name = "f_sys_del_user")
	private Long delUser;

	/** 删除状态=={'1':'正常','2':'已删除'} */
	@Column(name = "f_sys_del_state")
	private Byte delState;

	public enum Columns {
		/** 主键 */
		id,
		/** 创建时间 */
		addTime,
		/** 更新时间 */
		updTime,
		/** 删除时间 */
		delTime,
		/** 新增者 */
		addUser,
		/** 更新者 */
		updUser,
		/** 删除者 */
		delUser,
		/** 删除状态 */
		delState;
	}

}