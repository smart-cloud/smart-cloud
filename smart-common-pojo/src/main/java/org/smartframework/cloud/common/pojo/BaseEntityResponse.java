package org.smartframework.cloud.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.constant.DateConstant;

import java.util.Date;

/**
 * 实体对象对应的响应对象基类
 *
 * @author liyulin
 * @date 2020-05-24
 */
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseEntityResponse extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateConstant.DEFAULT_DATE_FORMAT)
    private Date insertTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateConstant.DEFAULT_DATE_FORMAT)
    private Date updTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = DateConstant.DEFAULT_DATE_FORMAT)
    private Date delTime;

    /**
     * 新增者
     */
    private Long insertUser;

    /**
     * 更新者
     */
    private Long updUser;

    /**
     * 删除者
     */
    private Long delUser;

    /**
     * 删除状态=={'1':'正常','2':'已删除'}
     */
    private Integer delState;

}