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
package org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartframework.cloud.common.pojo.BasePageResponse;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.smartframework.cloud.starter.mybatis.plus.enums.DeleteState;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.constants.DatasourceNames;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.mapper.ProductInfoBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo.PageProductReqVO;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo.ProductInfoBaseRespVO;
import org.springframework.stereotype.Repository;

/**
 * 商品信息oms biz
 *
 * @author liyulin
 * @date 2019-03-31
 */
@Repository
public class ProductInfoOmsBiz extends BaseBiz<ProductInfoBaseMapper, ProductInfoEntity> {

    @DS(DatasourceNames.PRODUCT_SLAVE)
    public BasePageResponse<ProductInfoBaseRespVO> selectPage(PageProductReqVO reqVO) {
        LambdaQueryWrapper<ProductInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ProductInfoEntity::getName, reqVO.getName());
        wrapper.eq(ProductInfoEntity::getDelState, DeleteState.NORMAL);
        wrapper.orderByDesc(ProductInfoEntity::getInsertTime);
        return super.page(reqVO, wrapper, ProductInfoBaseRespVO.class);
    }

}