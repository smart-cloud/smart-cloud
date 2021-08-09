package org.smartframework.cloud.starter.mybatis.test.prepare.dynamicdatasource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.SmartMapper;
import org.smartframework.cloud.starter.mybatis.test.prepare.dynamicdatasource.entity.RoleInfoEntity;

/**
 * 角色表base mapper
 *
 * @author liyulin
 * @date 2021-07-02
 */
@Mapper
public interface RoleInfoBaseMapper extends SmartMapper<RoleInfoEntity> {

}