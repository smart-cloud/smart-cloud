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
package io.github.smart.cloud.starter.configure.constants;

import org.springframework.core.Ordered;

/**
 * bean执行顺序
 *
 * @author collin
 * @date 2019-06-28
 */
public interface OrderConstant {

    /**
     * 用户上下文清理过滤器
     */
    int CLEAN_USER_CONTEXT_FILTER = Ordered.HIGHEST_PRECEDENCE;
    /**
     * http filter
     */
    int HTTP_FITLER = CLEAN_USER_CONTEXT_FILTER + 1;
    /**
     * api接口日志
     */
    int API_LOG = 1;
    /**
     * feign header参数
     */
    int FEIGN_HEADER = API_LOG + 1;
    /**
     * feign接口日志
     */
    int FEIGN_LOG = FEIGN_HEADER + 1;
    /**
     * 多语言切面
     */
    int LOCALE = FEIGN_LOG + 1;
    /**
     * 自定义sql日志拦截器（MybatisSqlLogInterceptor的优先级必须在高于MybatisPlusInterceptor，否则分页查询时，sql打印不全）
     */
    int MYBATIS_SQL_LOG_INTERCEPTOR = LOCALE + 1;
    /**
     * mybatis plus拦截器（MybatisSqlLogInterceptor的优先级必须在高于MybatisPlusInterceptor，否则分页查询时，sql打印不全）
     */
    int MYBATIS_PLUS_INTERCEPTOR = MYBATIS_SQL_LOG_INTERCEPTOR + 1;

}