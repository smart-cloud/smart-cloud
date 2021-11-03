package org.smartframework.cloud.api.core.annotation.constants;

/**
 * 接口注解常量
 *
 * @author collin
 * @date 2021-10-31
 */
public interface ApiAnnotationConstants {

    /**
     * 重复提交执行完后默认有效期
     */
    long DEFAULT_EXPIRE_MILLIS_OF_REPEAT_SUBMIT = 0L;

    /**
     * header 时间戳默认有效期（2分钟）
     */
    long DEFAULT_TIMESTAMP_VALID_MILLIS = 2 * 60 * 1000L;

}