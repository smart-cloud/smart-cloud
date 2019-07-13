package org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper;

import java.util.List;

import org.apache.ibatis.annotations.UpdateProvider;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.provider.BatchUpdateMapperProvider;

/**
 * 批量更新（null值也更新）
 *
 * @author liyulin
 * @date 2019-03-24
 */
public interface UpdateListByPrimaryKeyMapper<T> {

	/**
	 * 批量更新（null值也更新）
	 * 
	 * @param list
	 * @return
	 */
	@UpdateProvider(type = BatchUpdateMapperProvider.class, method = "dynamicSQL")
	int updateListByPrimaryKey(List<T> list);

}