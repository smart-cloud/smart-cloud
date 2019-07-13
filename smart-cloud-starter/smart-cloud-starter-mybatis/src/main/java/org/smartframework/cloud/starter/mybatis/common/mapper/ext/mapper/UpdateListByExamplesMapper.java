package org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.provider.BatchUpdateMapperProvider;

import tk.mybatis.mapper.entity.Example;

/**
 * 批量更新（null值也被更新）
 *
 * @author liyulin
 * @date 2019-04-07
 */
public interface UpdateListByExamplesMapper<T> {

	/**
	 * 根据对应的{@link Example}条件更新对应实体`record`包含的全部属性，null值会被更新
	 * 
	 * <p>
	 * 该操作，需要支持“allowMultiQueries=true”
	 * 
	 * @param list
	 * @return
	 */
	@UpdateProvider(type = BatchUpdateMapperProvider.class, method = "dynamicSQL")
	int updateListByExamples(@Param("records") List<T> records, @Param("examples") List<Example> examples);
	
}