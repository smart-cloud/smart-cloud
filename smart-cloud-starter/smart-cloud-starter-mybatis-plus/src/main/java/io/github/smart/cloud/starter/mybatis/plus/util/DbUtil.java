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
package io.github.smart.cloud.starter.mybatis.plus.util;

import com.mysql.cj.CharsetMapping;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库工具类
 *
 * @author collin
 * @date 2022-08-09
 */
@Slf4j
public class DbUtil {

    private DbUtil() {
    }

    /**
     * 创建数据库（如果不存在）
     *
     * @param dbName     数据库名
     * @param dataSource
     * @return
     */
    public static boolean createDbIfAbsent(String dbName, DataSource dataSource) {
        return createDbIfAbsent(dbName, CharsetMapping.MYSQL_CHARSET_NAME_utf8, dataSource);
    }

    /**
     * 创建数据库（如果不存在）
     *
     * @param dbName     数据库名
     * @param charset    字符集
     * @param dataSource
     * @return
     * @see CharsetMapping
     */
    public static boolean createDbIfAbsent(String dbName, String charset, DataSource dataSource) {
        boolean result = false;
        String createDbSql = String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET %s", dbName, charset);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstat = connection.prepareStatement(createDbSql)) {
            pstat.executeUpdate();
            result = true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.warn("execute sql==>{}", createDbSql);
        }
        return result;
    }

}