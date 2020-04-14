package org.smartframework.cloud.starter.web.aspect.pojo;

import java.util.Date;

import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.starter.core.business.security.bo.ReqHttpHeadersBO;
import org.smartframework.cloud.utility.constant.DateFormartConst;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 普通接口（除了feign以外的接口）切面日志DO
 *
 * @author liyulin
 * @date 2019-04-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LogAspectDO extends Base {

	private static final long serialVersionUID = 1L;

	/** 接口描述 */
	private String apiDesc;

	/** 调用的类方法 */
	private String classMethod;

	/** 请求发起时间 */
	@JSONField(format = DateFormartConst.DATETIME_SSS)
	private Date reqStartTime;

	/** 请求结束时间 */
	@JSONField(format = DateFormartConst.DATETIME_SSS)
	private Date reqEndTime;

	/** 请求处理时间,毫秒 */
	private Integer reqDealTime;

	/** 请求的参数信息 */
	private Object reqParams;
	
	/** 请求头 */
	private ReqHttpHeadersBO reqHttpHeaders;

	/** 响应数据 */
	private Object respData;
	
	/** 接口url */
	private String url;

	/** http请求方式 */
	private String httpMethod;

	/** 客户IP地址 */
	private String ip;

	/** 操作系统相关信息 */
	private String os;

}