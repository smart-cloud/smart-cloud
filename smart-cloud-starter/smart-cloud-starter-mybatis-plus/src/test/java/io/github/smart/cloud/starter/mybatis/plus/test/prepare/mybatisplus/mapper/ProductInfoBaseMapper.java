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
package io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.mapper;

import io.github.smart.cloud.starter.mybatis.plus.common.mapper.SmartMapper;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.entity.ProductInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

import java.io.Reader;
import java.util.Date;

/**
 * 商品信息base mapper
 *
 * @author collin
 * @date 2021-03-23
 */
@Mapper
public interface ProductInfoBaseMapper extends SmartMapper<ProductInfoEntity> {

    /**
     * 游标查询（sql中故意包含换行、制表符、连续空格，用于验证sql日志的空白压缩）
     *
     * @param name
     * @return
     */
    @Select("SELECT f_id AS id, f_name AS name\n\t  FROM   t_product_info\r\n WHERE f_name = #{name}")
    Cursor<ProductInfoEntity> cursorByName(String name);

    /**
     * 多参数查询（参数为ParamMap，sql日志走参数内联分支；sql中故意包含垂直制表符、换页符）
     *
     * @param name
     * @param time
     * @return
     */
    @Select("SELECT COUNT(*)\u000B\fFROM t_product_info WHERE f_name = #{name} AND f_sys_insert_time <= #{time}")
    Long countByNameAndTime(@Param("name") String name, @Param("time") Date time);

    /**
     * 不可序列化、但有TypeHandler的单个参数（Reader）
     *
     * @param name
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_product_info WHERE f_name = #{name}")
    Long countByReader(Reader name);

}
