package org.smartframework.cloud.starter.configure.constants;

/**
 * bean执行顺序
 *
 * @author liyulin
 * @date 2019-06-28
 */
public interface OrderConstant {
    /**
     * 接口日志
     */
    int API_LOG = 1;
    /**
     * 多语言切面
     */
    int LOCALE = API_LOG + 1;
    /**
     * 自定义sql日志拦截器（MybatisSqlLogInterceptor的优先级必须在高于MybatisPlusInterceptor，否则分页查询时，sql打印不全）
     */
    int MYBATIS_SQL_LOG_INTERCEPTOR = LOCALE + 1;
    /**
     * mybatis plus拦截器（MybatisSqlLogInterceptor的优先级必须在高于MybatisPlusInterceptor，否则分页查询时，sql打印不全）
     */
    int MYBATIS_PLUS_INTERCEPTOR = MYBATIS_SQL_LOG_INTERCEPTOR + 1;
    /**
     * http filter
     */
    int HTTP_FITLER = Integer.MIN_VALUE;

}