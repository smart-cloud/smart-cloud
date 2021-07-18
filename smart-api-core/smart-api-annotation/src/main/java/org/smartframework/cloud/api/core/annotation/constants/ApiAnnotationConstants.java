package org.smartframework.cloud.api.core.annotation.constants;

public interface ApiAnnotationConstants {

    /**
     * 重复提交执行完后默认有效期
     */
    long DEFAULT_EXPIRE_MILLIS_OF_REPEAT_SUBMIT = 0L;

    /**
     * header 时间戳有效期（2分钟）
     */
    long DEFAULT_TIMESTAMP_VALID_MILLIS = 2 * 60 * 1000L;

}