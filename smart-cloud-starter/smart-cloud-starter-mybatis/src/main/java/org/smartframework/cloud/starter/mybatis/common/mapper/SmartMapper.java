package org.smartframework.cloud.starter.mybatis.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * smart cloud BaseMapper
 *
 * @author collin
 * @date 2021-03-23
 */
public interface SmartMapper<T> extends BaseMapper<T> {

    /**
     * in-line式批量插入
     *
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(@Param("list") List<T> entityList);

}