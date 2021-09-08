package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.entity.BaseEntity;

/**
 * 订单信息
 *
 * @author liyulin
 * @date 2021-03-23
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@TableName("t_api_log")
public class ApiLogEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 接口描述
     */
    @TableField(value = "f_api_desc")
    private String apiDesc;

}