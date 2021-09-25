package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.SmartMapper;

/**
 * 商品信息base mapper
 *
 * @author liyulin
 * @date 2021-03-23
 */
@Mapper
public interface ProductInfoBaseMapper extends SmartMapper<ProductInfoEntity> {

}