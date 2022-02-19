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
package io.github.smart.cloud.starter.mybatis.plus.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import io.github.smart.cloud.starter.mybatis.plus.common.CryptField;
import io.github.smart.cloud.starter.mybatis.plus.util.FieldCryptUtil;

import java.sql.*;

/**
 * 表隐私字段加解密hander
 *
 * @author collin
 * @date 2022-02-06
 */
@MappedTypes(value = CryptField.class)
public class CryptFieldHandler extends BaseTypeHandler<CryptField> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CryptField parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.getValue() == null) {
            ps.setNull(i, Types.VARCHAR);
        } else {
            ps.setString(i, FieldCryptUtil.encrypt(parameter.getValue()));
        }
    }

    @Override
    public CryptField getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        if (columnValue != null) {
            columnValue = FieldCryptUtil.decrypt(columnValue);
        }
        return CryptField.of(columnValue);
    }

    @Override
    public CryptField getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (columnValue != null) {
            columnValue = FieldCryptUtil.decrypt(columnValue);
        }
        return CryptField.of(columnValue);
    }

    @Override
    public CryptField getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        if (columnValue != null) {
            columnValue = FieldCryptUtil.decrypt(columnValue);
        }
        return CryptField.of(columnValue);
    }

}