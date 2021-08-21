package org.smartframework.cloud.starter.mybatis.plus.common.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseEntity extends Base {

    private static final long serialVersionUID = 1L;

    @TableId(value = "f_id")
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "f_sys_insert_time")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField(value = "f_sys_upd_time")
    private Date updTime;

    /**
     * 删除时间
     */
    @TableField(value = "f_sys_del_time")
    private Date delTime;

    /**
     * 新增者
     */
    @TableField(value = "f_sys_insert_user")
    private Long insertUser;

    /**
     * 更新者
     */
    @TableField(value = "f_sys_upd_user")
    private Long updUser;

    /**
     * 删除者
     */
    @TableField(value = "f_sys_del_user")
    private Long delUser;

    /**
     * 删除状态=={'1':'正常','2':'已删除'}
     */
    @TableField(value = "f_sys_del_state")
    @TableLogic(value = "1", delval = "2")
    private Byte delState;

}