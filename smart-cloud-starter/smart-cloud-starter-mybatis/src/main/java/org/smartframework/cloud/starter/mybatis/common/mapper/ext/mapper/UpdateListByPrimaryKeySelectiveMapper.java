package org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper;

import java.util.List;

import org.apache.ibatis.annotations.UpdateProvider;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.provider.BatchUpdateMapperProvider;

/**
 * 批量更新（null值不更新）
 *
 * @author liyulin
 * @date 2019-03-24
 */
public interface UpdateListByPrimaryKeySelectiveMapper<T> {

	/**
	 * 批量更新（null值不更新）
	 * 
	 * @param list
	 * @return
	 */
	@UpdateProvider(type = BatchUpdateMapperProvider.class, method = "dynamicSQL")
	int updateListByPrimaryKeySelective(List<T> list);

}