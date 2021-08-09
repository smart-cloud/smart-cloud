package org.smartframework.cloud.starter.mybatis.test.prepare.dynamicdatasourcemasterslave.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.SmartMapper;
import org.smartframework.cloud.starter.mybatis.test.prepare.dynamicdatasourcemasterslave.entity.ProductInfoEntity;

/**
 * 商品信息base mapper
 *
 * @author liyulin
 * @date 2021-03-23
 */
@Mapper
public interface ProductInfoBaseMapper extends SmartMapper<ProductInfoEntity> {

}