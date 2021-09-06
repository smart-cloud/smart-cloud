package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz;

import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.mapper.ProductInfoBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.springframework.stereotype.Repository;

/**
 * 商品信息oms biz
 *
 * @author liyulin
 * @date 2019-03-31
 */
@Repository
public class ProductInfoBiz extends BaseBiz<ProductInfoBaseMapper, ProductInfoEntity> {

}