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

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.smart.cloud.starter.mybatis.plus.common.CryptField;
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.CryptFieldApp;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.biz.DescCryptField;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.biz.ProductInfoOmsBiz;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.entity.ProductInfoEntity;
import io.github.smart.cloud.starter.mybatis.plus.util.FieldCryptUtil;
import io.github.smart.cloud.utility.NonceUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CryptFieldApp.class, args = "--spring.profiles.active=crypt-field")
class CryptFieldHandlerTest {

    @Autowired
    private ProductInfoOmsBiz productInfoOmsBiz;
    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;
    private static final String ORIGINAL_VALUE = "phone6";

    @Test
    void test() throws SQLException {
        ProductInfoEntity entity = new ProductInfoEntity();
        entity.setId(NonceUtil.nextId());
        entity.setInsertTime(new Date());
        entity.setDelState(DeleteState.NORMAL);
        entity.setName(new CryptField(ORIGINAL_VALUE));
        entity.setDesc(new DescCryptField(ORIGINAL_VALUE));
        entity.setSellPrice(10L);
        entity.setStock(10L);
        entity.setUpdTime(new Date());
        entity.setDelTime(new Date());
        entity.setInsertUser(1L);
        Assertions.assertThat(productInfoOmsBiz.save(entity)).isTrue();

        Long id = entity.getId();

        ProductInfoEntity dbEntity = productInfoOmsBiz.getById(id);
        Assertions.assertThat(dbEntity).isNotNull();
        Assertions.assertThat(dbEntity.getName().getValue()).isEqualTo(entity.getName().getValue());
        Assertions.assertThat(dbEntity.getDesc().getValue()).isEqualTo(entity.getDesc().getValue());


        // 加密字段查询
        LambdaQueryWrapper<ProductInfoEntity> condition = new LambdaQueryWrapper<>();
        condition.select(ProductInfoEntity::getId, ProductInfoEntity::getName, ProductInfoEntity::getDesc)
                .eq(ProductInfoEntity::getName, new CryptField(ORIGINAL_VALUE));
        ProductInfoEntity productInfoEntity = productInfoOmsBiz.getOne(condition);
        Assertions.assertThat(productInfoEntity).isNotNull();
        Assertions.assertThat(productInfoEntity.getName().getValue()).isEqualTo(entity.getName().getValue());
        Assertions.assertThat(productInfoEntity.getDesc().getValue()).isEqualTo(entity.getDesc().getValue());

        try (Connection connection = dynamicRoutingDataSource.determineDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select f_name,f_desc from t_product_info where f_id=?")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Assertions.assertThat(resultSet.next()).isTrue();
                String name = resultSet.getString(1);
                String desc = resultSet.getString("f_desc");
                Assertions.assertThat(name).isNotBlank();
                Assertions.assertThat(name).isEqualTo(FieldCryptUtil.encrypt(ORIGINAL_VALUE, CryptField.class.getName()));
                Assertions.assertThat(desc).isEqualTo(FieldCryptUtil.encrypt(ORIGINAL_VALUE, DescCryptField.class.getName()));
            }
        }
    }

}