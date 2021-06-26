package org.smartframework.cloud.starter.configure.properties;

import org.smartframework.cloud.common.pojo.Base;

/**
 * mq配置
 *
 * @author collin
 * @date 2021-06-29
 */
public class MqProperties extends Base {

    /**
     * 幂等有效期（单位：毫秒），默认1小时
     */
    private Long idempotentCheckCacheExpire;

    public Long getIdempotentCheckCacheExpire() {
        return idempotentCheckCacheExpire == null ? 60 * 60 * 1000 : idempotentCheckCacheExpire;
    }

    public void setIdempotentCheckCacheExpire(Long idempotentCheckCacheExpire) {
        this.idempotentCheckCacheExpire = idempotentCheckCacheExpire;
    }

}