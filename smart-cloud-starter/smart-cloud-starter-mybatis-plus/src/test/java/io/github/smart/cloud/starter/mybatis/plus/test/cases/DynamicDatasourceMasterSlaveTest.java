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
package io.github.smart.cloud.starter.mybatis.plus.test.cases;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.smart.cloud.common.pojo.BasePageResponse;
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.RedisMockServerAutoConfiguration;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.DynamicDatasourceMasterSlaveApp;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.biz.ProductInfoOmsBiz;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.entity.ProductInfoEntity;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo.PageProductReqVO;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo.ProductInfoBaseRespVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@Import(RedisMockServerAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DynamicDatasourceMasterSlaveApp.class, args = "--spring.profiles.active=dynamicdatasourcemasterslave")
public class DynamicDatasourceMasterSlaveTest {

    @Autowired
    private ProductInfoOmsBiz productInfoOmsBiz;

    @BeforeEach
    void cleanData() {
        productInfoOmsBiz.truncate();
    }

    @Test
    void testCreate() {
        boolean success = productInfoOmsBiz.save(create("test"));
        Assertions.assertThat(success).isTrue();
    }

    @Test
    void testInsertBatchSomeColumn() {
        List<ProductInfoEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(create("test" + i));
        }
        int successCount = productInfoOmsBiz.insertBatchSomeColumn(entities);
        Assertions.assertThat(successCount).isEqualTo(entities.size());
    }

    @Test
    void testLogicDelete() {
        ProductInfoEntity entity = create("testx");
        boolean createSuccess = productInfoOmsBiz.save(entity);
        Assertions.assertThat(createSuccess).isTrue();

        Boolean deleteSuccess = productInfoOmsBiz.logicDelete(entity.getId(), 10L);
        Assertions.assertThat(deleteSuccess).isTrue();
    }

    @Test
    void testRemove() {
        ProductInfoEntity entity = create("testx");
        boolean createSuccess = productInfoOmsBiz.save(entity);
        Assertions.assertThat(createSuccess).isTrue();

        Assertions.assertThat(productInfoOmsBiz.removeById(entity.getId())).isTrue();
    }

    @Test
    void testPage() {
        String name = "testx";
        ProductInfoEntity entity = create(name);
        boolean createSuccess = productInfoOmsBiz.save(entity);
        Assertions.assertThat(createSuccess).isTrue();

        PageProductReqVO reqVO = new PageProductReqVO();
        reqVO.setName(name);
        reqVO.setPageNum(1);
        reqVO.setPageSize(10);

        // master--start
        LambdaQueryWrapper<ProductInfoEntity> wrapperFromMaster = new LambdaQueryWrapper<>();
        wrapperFromMaster.like(ProductInfoEntity::getName, reqVO.getName());
        wrapperFromMaster.eq(ProductInfoEntity::getDelState, DeleteState.NORMAL);
        wrapperFromMaster.orderByDesc(ProductInfoEntity::getInsertTime);
        BasePageResponse<ProductInfoBaseRespVO> responseOfMaster = productInfoOmsBiz.page(reqVO, wrapperFromMaster, ProductInfoBaseRespVO.class);

        Assertions.assertThat(responseOfMaster).isNotNull();
        Assertions.assertThat(responseOfMaster.getDatas()).isNotEmpty();
        Assertions.assertThat(responseOfMaster.getPageSize()).isEqualTo(reqVO.getPageSize());
        Assertions.assertThat(responseOfMaster.getPageIndex()).isEqualTo(reqVO.getPageNum());
        Assertions.assertThat(responseOfMaster.getPageTotal()).isEqualTo(1);
        // master--end

        // master--start
        BasePageResponse<ProductInfoBaseRespVO> responseOfSlave = productInfoOmsBiz.selectPage(reqVO);

        Assertions.assertThat(responseOfSlave).isNotNull();
        Assertions.assertThat(responseOfSlave.getDatas()).isEmpty();
        Assertions.assertThat(responseOfSlave.getPageSize()).isEqualTo(reqVO.getPageSize());
        Assertions.assertThat(responseOfSlave.getPageIndex()).isEqualTo(reqVO.getPageNum());
        Assertions.assertThat(responseOfSlave.getPageTotal()).isEqualTo(0);
        // master--end
    }

    private ProductInfoEntity create(String name) {
        ProductInfoEntity entity = productInfoOmsBiz.buildEntity();
        entity.setName(name);
        entity.setSellPrice(100L);
        entity.setStock(10L);
        entity.setInsertUser(10L);
        return entity;
    }

}