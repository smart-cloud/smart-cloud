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
package org.smartframework.cloud.starter.mybatis.plus.test.cases;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mybatis.plus.common.CryptField;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.CryptFieldApp;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.biz.ProductInfoOmsBiz;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.fieldcrypt.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mybatis.plus.util.FieldCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    private final String ORIGINAL_VALUE = "phone6";

    @Test
    void test() throws SQLException {
        ProductInfoEntity entity = productInfoOmsBiz.buildEntity();
        entity.setName(CryptField.of(ORIGINAL_VALUE));
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

        PreparedStatement preparedStatement = dynamicRoutingDataSource.determineDataSource().getConnection()
                .prepareStatement("select f_name from t_product_info where f_id=?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Assertions.assertThat(resultSet.next()).isTrue();
        String name = resultSet.getString(1);
        Assertions.assertThat(name).isNotBlank();
        Assertions.assertThat(name).isEqualTo(FieldCryptUtil.encrypt(ORIGINAL_VALUE));
    }

}