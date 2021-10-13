package org.smartframework.cloud.starter.mybatis.plus.test.cases;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.smartframework.cloud.common.pojo.BasePageResponse;
import org.smartframework.cloud.starter.core.business.util.SnowFlakeIdUtil;
import org.smartframework.cloud.starter.mybatis.plus.enums.DeleteState;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.DynamicDatasourceApp;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.biz.ProductInfoOmsBiz;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.biz.RoleInfoOmsBiz;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.entity.RoleInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.vo.PageProductReqVO;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.vo.ProductInfoBaseRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(JUnitPlatform.class)
public class DynamicDatasourceTest {

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(classes = DynamicDatasourceApp.class, args = "--spring.profiles.active=dynamicdatasource")
    @Nested
    class ProductTest {

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

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(classes = DynamicDatasourceApp.class, args = "--spring.profiles.active=dynamicdatasource")
    @Nested
    public class AuthTest {

        @Autowired
        private RoleInfoOmsBiz roleInfoOmsBiz;

        @BeforeEach
        void cleanData() {
            roleInfoOmsBiz.truncate();
        }

        @Test
        public void testSave() {
            RoleInfoEntity roleInfoEntity = RoleInfoEntity.builder()
                    .id(SnowFlakeIdUtil.getInstance().nextId())
                    .code(String.format("query%s", UUID.randomUUID().toString().replaceAll("-", "")))
                    .description("查询")
                    .insertTime(new Date())
                    .insertUser(1L)
                    .delState(DeleteState.NORMAL)
                    .build();

            roleInfoOmsBiz.save(roleInfoEntity);
        }
    }

}