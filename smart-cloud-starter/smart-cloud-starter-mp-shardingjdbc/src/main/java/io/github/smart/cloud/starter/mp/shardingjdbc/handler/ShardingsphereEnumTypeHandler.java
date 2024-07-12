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
package io.github.smart.cloud.starter.mp.shardingjdbc.handler;

import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import io.github.smart.cloud.utility.ClassUtil;
import org.apache.shardingsphere.driver.jdbc.core.resultset.ShardingSphereResultSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * shardingsphere处理枚举
 *
 * @param <E>
 * @author collin
 * @date 2021-11-13
 */
public class ShardingsphereEnumTypeHandler<E extends Enum<E>> extends MybatisEnumTypeHandler<E> {

    /**
     * MybatisEnumTypeHandler#propertyType
     */
    private final String propertyTypeFieldName = "propertyType";
    /**
     * MybatisEnumTypeHandler#valueOf
     */
    private final String valueOfMethodName = "valueOf";

    private final Class<?> propertyType;
    private final Method valueOfMethod;

    public ShardingsphereEnumTypeHandler(Class<E> enumClassType) {
        super(enumClassType);

        this.propertyType = ClassUtil.findFieldValue(this, MybatisEnumTypeHandler.class, propertyTypeFieldName);
        this.valueOfMethod = ClassUtil.findMethod(MybatisEnumTypeHandler.class, valueOfMethodName, Object.class);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 修改以便shardingsphere支持枚举----start
        if (rs instanceof ShardingSphereResultSet) {
            ShardingSphereResultSet shardingSphereResultSet = (ShardingSphereResultSet) rs;
            try {
                Object value = null;
                if (Byte.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getByte(columnName);
                }
                if (Short.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getShort(columnName);
                }
                if (Integer.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getInt(columnName);
                }
                if (Long.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getLong(columnName);
                }
                if (Double.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getDouble(columnName);
                }
                if (Float.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getFloat(columnName);
                }
                if (String.class.equals(propertyType)) {
                    value = shardingSphereResultSet.getString(columnName);
                }
                return (E) valueOfMethod.invoke(this, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        // 修改以便shardingsphere支持枚举----end

        return super.getNullableResult(rs, columnName);
    }

}