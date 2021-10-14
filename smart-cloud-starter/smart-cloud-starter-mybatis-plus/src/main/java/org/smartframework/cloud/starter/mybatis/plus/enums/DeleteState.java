package org.smartframework.cloud.starter.mybatis.plus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态
 *
 * @author collin
 * @date 2021-10-02
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeleteState {

    /**
     * 未删除
     */
    NORMAL((byte) 1),
    /**
     * 已删除
     */
    DELETED((byte) 2);

    /**
     * 删除状态
     */
    @EnumValue
    private byte state;

}