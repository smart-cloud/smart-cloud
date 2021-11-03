package org.smartframework.cloud.common.pojo.util;

import org.smartframework.cloud.common.pojo.BasePageRequest;
import org.smartframework.cloud.common.pojo.dataobject.BasePageRequestDO;

/**
 * 分页工具类
 *
 * @author collin
 * @date 2021-10-30
 */
public class PageUtil {

    private PageUtil() {
    }

    /**
     * 设置limit分页参数
     *
     * @param basePageRequestDO
     * @param pageRequest
     * @return
     */
    public static BasePageRequestDO initLimitParams(BasePageRequestDO basePageRequestDO, BasePageRequest pageRequest) {
        basePageRequestDO.setOffset((pageRequest.getPageNum() - 1) * pageRequest.getPageSize());
        basePageRequestDO.setRows(pageRequest.getPageSize());
        return basePageRequestDO;
    }

}