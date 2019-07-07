package org.smartframework.cloud.starter.mybatis.common.biz;

import java.util.Date;

import org.smartframework.cloud.starter.common.business.util.SnowFlakeIdUtil;
import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;
import org.smartframework.cloud.starter.mybatis.common.mapper.enums.DelStateEnum;
import org.smartframework.cloud.starter.mybatis.util.ClassUtil;
import org.springframework.beans.BeanUtils;

public class BaseBiz<T extends BaseEntity> {

	/**
	 * 生成id
	 * 
	 * @return
	 */
	protected long generateId() {
		return SnowFlakeIdUtil.getInstance().nextId();
	}
	
	public T create() {
		Class<T> clazz = ClassUtil.getActualTypeArgumentFromSuperGenericClass(getClass(), 0);
		T entity = BeanUtils.instantiateClass(clazz);
		entity.setId(SnowFlakeIdUtil.getInstance().nextId());
		entity.setAddTime(new Date());
		entity.setDelState(DelStateEnum.NORMAL.getDelState());
		return entity;
	}

}