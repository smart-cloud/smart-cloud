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
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.MybatisplusApp;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.biz.ProductInfoOmsBiz;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.entity.ProductInfoEntity;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.vo.PageProductReqVO;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.vo.ProductInfoBaseRespVO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.common.pojo.BasePageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisplusApp.class, args = "--spring.profiles.active=mybatisplus")
class MybatisPlusTest {

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

        LambdaQueryWrapper<ProductInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ProductInfoEntity::getName, reqVO.getName());
        wrapper.eq(ProductInfoEntity::getDelState, DeleteState.NORMAL);
        wrapper.orderByDesc(ProductInfoEntity::getInsertTime);
        BasePageResponse<ProductInfoBaseRespVO> response = productInfoOmsBiz.page(reqVO, wrapper, ProductInfoBaseRespVO.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getDatas()).isNotEmpty();
        Assertions.assertThat(response.getPageSize()).isEqualTo(reqVO.getPageSize());
        Assertions.assertThat(response.getPageIndex()).isEqualTo(reqVO.getPageNum());
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