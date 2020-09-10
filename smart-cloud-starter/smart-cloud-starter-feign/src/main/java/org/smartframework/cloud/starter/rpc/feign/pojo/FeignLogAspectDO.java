package org.smartframework.cloud.starter.rpc.feign.pojo;

import java.util.Date;

import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.starter.core.business.security.bo.ReqHttpHeadersBO;
import org.smartframework.cloud.utility.constant.DateFormartConst;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * feign切面日志Dto
 *
 * @author liyulin
 * @date 2019-04-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FeignLogAspectDO extends Base {

	private static final long serialVersionUID = 1L;

	/** 调用的类方法 */
	private String classMethod;

	/** 请求发起时间 */
	@JsonFormat(pattern = DateFormartConst.DATETIME_SSS)
	private Date reqStartTime;

	/** 请求结束时间 */
	@JsonFormat(pattern = DateFormartConst.DATETIME_SSS)
	private Date reqEndTime;

	/** 请求处理时间,毫秒 */
	private Integer reqDealTime;

	/** 请求的参数信息 */
	private Object reqParams;
	
	/** 请求头 */
	private ReqHttpHeadersBO reqHttpHeaders;

	/** 响应数据 */
	private Object respData;

}