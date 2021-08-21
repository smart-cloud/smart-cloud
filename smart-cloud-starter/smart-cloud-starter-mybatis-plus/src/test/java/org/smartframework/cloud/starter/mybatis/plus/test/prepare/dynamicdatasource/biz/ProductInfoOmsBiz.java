package org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.constants.DatasourceNames;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.mapper.ProductInfoBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 商品信息oms biz
 *
 * @author liyulin
 * @date 2019-03-31
 */
@Repository
@DS(DatasourceNames.PRODUCT)
public class ProductInfoOmsBiz extends BaseBiz<ProductInfoBaseMapper, ProductInfoEntity> {

}