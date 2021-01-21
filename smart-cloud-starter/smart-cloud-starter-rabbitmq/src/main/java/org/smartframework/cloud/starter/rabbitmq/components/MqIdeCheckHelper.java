package org.smartframework.cloud.starter.rabbitmq.components;

import org.smartframework.cloud.starter.redis.RedisKeyUtil;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.smartframework.cloud.starter.redis.enums.RedisKeyPrefix;

/**
 * mq幂等校验
 *
 * @author liyulin
 * @date 2020-08-12
 */
public class MqIdeCheckHelper {
    /**
     * 幂等有效期，1小时
     */
    private static final long IDEMPOTENT_CHECK_CACHE_TIME = 60 * 60 * 1000;
    /**
     * 幂等校验key前缀
     */
    private static final String IDE_CKECK_PREFIX_KEY = RedisKeyUtil.buildKey(RedisKeyPrefix.LOCK.getKey(), "idecheck", "mq");

    private RedisComponent redisComponent;

    public MqIdeCheckHelper(RedisComponent redisComponent) {
        this.redisComponent = redisComponent;
    }

    /**
     * 幂等校验
     *
     * @param taskId mq taskId
     * @return
     */
    public boolean check(String taskId) {
        if (taskId == null || taskId.length() == 0) {
            return true;
        }

        return redisComponent.setNx(getKey(taskId), "", IDEMPOTENT_CHECK_CACHE_TIME);
    }

    private String getKey(String msgUniqueKey) {
        return RedisKeyUtil.buildKey(IDE_CKECK_PREFIX_KEY, msgUniqueKey);
    }

}