package org.smartframework.cloud.common.pojo.util;

import org.smartframework.cloud.common.pojo.BasePageRequest;
import org.smartframework.cloud.common.pojo.dataobject.BasePageRequestDO;

public class PageUtil {

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