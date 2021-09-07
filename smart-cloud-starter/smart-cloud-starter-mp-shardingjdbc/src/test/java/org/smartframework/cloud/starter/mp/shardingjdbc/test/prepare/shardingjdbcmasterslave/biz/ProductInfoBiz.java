package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.mapper.ProductInfoBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.smartframework.cloud.utility.RandomUtil;
import org.springframework.stereotype.Repository;

/**
 * 商品信息oms biz
 *
 * @author liyulin
 * @date 2019-03-31
 */
@Repository
@DS(ShardingjdbcDatasourceNames.SHARDING_DATASOURCE)
public class ProductInfoBiz extends BaseBiz<ProductInfoBaseMapper, ProductInfoEntity> {

    public Long create() {
        ProductInfoEntity entity = super.buildEntity();
        entity.setName(RandomUtil.generateRandom(false, 6));
        entity.setSellPrice(100L);
        entity.setStock(100L);
        entity.setInsertUser(1L);
        super.save(entity);

        return entity.getId();
    }

    public ProductInfoEntity query(Long id) {
        return super.getById(id);
    }

}