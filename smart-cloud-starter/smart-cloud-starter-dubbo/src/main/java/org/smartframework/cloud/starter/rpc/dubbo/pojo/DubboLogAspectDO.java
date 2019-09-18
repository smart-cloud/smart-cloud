package org.smartframework.cloud.starter.rpc.dubbo.pojo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * dubbo切面日志
 *
 * @author liyulin
 * @date 2019-04-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DubboLogAspectDO implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssS";

	/** 调用的类方法 */
	private String classMethod;

	/** 请求发起时间 */
	@JSONField(format = LOG_DATE_FORMAT)
	private Date reqStartTime;

	/** 请求结束时间 */
	@JSONField(format = LOG_DATE_FORMAT)
	private Date reqEndTime;

	/** 请求处理时间,毫秒 */
	private Integer reqDealTime;

	/** 请求的参数信息 */
	private Object reqParams;
	
	/** 响应数据 */
	private Object respData;
	
}