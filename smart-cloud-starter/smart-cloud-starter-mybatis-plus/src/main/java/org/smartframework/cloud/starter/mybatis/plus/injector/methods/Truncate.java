package org.smartframework.cloud.starter.mybatis.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * truncate表数据
 *
 * @author collin
 * @date 21021-09-30
 */
public class Truncate extends AbstractMethod {

    final String TRUNCATE_METHOD_NAME = "truncate";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format("TRUNCATE TABLE %s", tableInfo.getTableName());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, TRUNCATE_METHOD_NAME, sqlSource);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        return TRUNCATE_METHOD_NAME;
    }

}