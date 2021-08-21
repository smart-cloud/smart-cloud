package org.smartframework.cloud.starter.mybatis.plus.test.cases;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.BasePageResponse;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.constants.DelState;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.DynamicDatasourceMasterSlaveApp;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.biz.ProductInfoOmsBiz;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo.PageProductReqVO;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo.ProductInfoBaseRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DynamicDatasourceMasterSlaveApp.class, args = "--spring.profiles.active=dynamicdatasourcemasterslave")
public class DynamicDatasourceMasterSlaveTest {

    @Autowired
    private ProductInfoOmsBiz productInfoOmsBiz;

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
        wrapper.eq(ProductInfoEntity::getDelState, DelState.NORMAL);
        wrapper.orderByDesc(ProductInfoEntity::getInsertTime);
        BasePageResponse<ProductInfoBaseRespVO> response = productInfoOmsBiz.page(reqVO, wrapper, ProductInfoBaseRespVO.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getDatas()).isNotEmpty();
        Assertions.assertThat(response.getPageSize()).isEqualTo(reqVO.getPageSize());
        Assertions.assertThat(response.getPageIndex()).isEqualTo(reqVO.getPageNum());
    }

    private ProductInfoEntity create(String name) {
        ProductInfoEntity entity = productInfoOmsBiz.create();
        entity.setName(name);
        entity.setSellPrice(100L);
        entity.setStock(10L);
        entity.setInsertUser(10L);
        return entity;
    }
}