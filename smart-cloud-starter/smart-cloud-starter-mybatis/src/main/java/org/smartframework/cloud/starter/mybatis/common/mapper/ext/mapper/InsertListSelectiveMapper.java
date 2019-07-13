package org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.provider.InsertListSelectiveMapperProvider;

/**
 * 批量插入（不为null的值）
 *
 * @author liyulin
 * @date 2019-03-24
 */
public interface InsertListSelectiveMapper<T> {

	/**
	 * 批量插入（不为null的值）
	 * 
	 * <p>
	 * 该操作，需要支持“allowMultiQueries=true”
	 * 
	 * @param list
	 * @return
	 */
	@InsertProvider(type = InsertListSelectiveMapperProvider.class, method = "dynamicSQL")
	int insertListSelective(List<? extends T> list);

}