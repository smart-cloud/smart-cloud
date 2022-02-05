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
package org.smartframework.cloud.starter.mybatis.plus.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.starter.mybatis.plus.enums.DeleteState;

import java.util.Date;

/**
 * entity基类
 *
 * @author collin
 * @date 2021-10-31
 */
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
    private DeleteState delState;

}