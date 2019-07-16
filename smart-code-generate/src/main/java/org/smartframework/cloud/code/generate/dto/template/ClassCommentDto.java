package org.smartframework.cloud.code.generate.dto.template;

import lombok.Getter;
import lombok.Setter;

/**
 * 类注释信息
 * 
 * @author liyulin
 * @date 2019-07-16
 */
@Getter
@Setter
public class ClassCommentDto {

	/** 创建人 */
	private String author;
	/** 生成时间(yyyy-MM-dd) */
	private String createDate;
	
}