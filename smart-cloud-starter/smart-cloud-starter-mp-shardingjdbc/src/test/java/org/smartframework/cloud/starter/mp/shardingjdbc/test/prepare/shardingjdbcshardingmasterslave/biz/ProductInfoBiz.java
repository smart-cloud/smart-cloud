/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.biz;

import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.mapper.ProductInfoBaseMapper;
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