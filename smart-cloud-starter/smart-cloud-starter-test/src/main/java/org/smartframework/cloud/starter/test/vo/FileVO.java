package org.smartframework.cloud.starter.test.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 上传文件file参数
 *
 * @author collin
 * @date 2021-06-23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class FileVO implements java.io.Serializable {

    /**
     * 表单文件参数名
     */
    private String name;
    /**
     * 文件
     */
    private java.io.File file;

}