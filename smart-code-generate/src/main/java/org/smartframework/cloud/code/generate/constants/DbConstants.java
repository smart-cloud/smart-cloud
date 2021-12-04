package org.smartframework.cloud.code.generate.constants;

/**
 * 数据库相关常量
 *
 * @author collin
 * @date 2021-12-12
 */
public interface DbConstants {

    /**
     * jdbc查询表的类型
     */
    String TABLE_TYPE = "TABLE";

    /**
     * 数据库连接属性
     *
     * @author collin
     * @date 2021-12-12
     */
    interface ConnectionProperties {
        /**
         * 数据库用户名
         */
        String USER = "user";
        /**
         * 数据库密码
         */
        String PASSWORD = "password";
        /**
         * 获取Oracle元数据 REMARKS信息
         */
        String REMARKS_REPORTING = "remarksReporting";
        /**
         * 获取MySQL元数据 REMARKS信息
         */
        String USE_INFORMATION_SCHEMA = "useInformationSchema";
    }

}