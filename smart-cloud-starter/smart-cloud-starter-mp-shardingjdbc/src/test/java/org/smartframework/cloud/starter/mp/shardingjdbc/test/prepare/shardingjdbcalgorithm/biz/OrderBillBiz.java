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
package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.mapper.OrderBillBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.springframework.stereotype.Repository;

/**
 * 订单信息api biz
 *
 * @author liyulin
 * @date 2019-04-08
 */
@Repository
public class OrderBillBiz extends BaseBiz<OrderBillBaseMapper, OrderBillEntity> {

    public OrderBillEntity getByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderBillEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderBillEntity::getOrderNo, orderNo);
        return getOne(queryWrapper);
    }

    public OrderBillEntity getByBuyer(Long buyer) {
        LambdaQueryWrapper<OrderBillEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderBillEntity::getBuyer, buyer);
        return getOne(queryWrapper);
    }

}