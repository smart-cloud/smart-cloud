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
package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.constants.DatasourceName;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.mapper.ProductInfoBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.springframework.stereotype.Repository;

/**
 * 商品信息oms biz
 *
 * @author collin
 * @date 2019-03-31
 */
@Repository
@DS(DatasourceName.SHARDING_DATASOURCE)
public class ProductInfoBiz extends BaseBiz<ProductInfoBaseMapper, ProductInfoEntity> {

}