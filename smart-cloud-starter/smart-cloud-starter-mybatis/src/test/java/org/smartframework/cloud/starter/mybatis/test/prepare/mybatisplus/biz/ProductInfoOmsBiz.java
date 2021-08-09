package org.smartframework.cloud.starter.mybatis.test.prepare.mybatisplus.biz;

import org.smartframework.cloud.starter.mybatis.common.biz.BaseBiz;
import org.smartframework.cloud.starter.mybatis.test.prepare.mybatisplus.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.test.prepare.mybatisplus.mapper.ProductInfoBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 商品信息oms biz
 *
 * @author liyulin
 * @date 2019-03-31
 */
@Repository
public class ProductInfoOmsBiz extends BaseBiz<ProductInfoBaseMapper, ProductInfoEntity> {

}