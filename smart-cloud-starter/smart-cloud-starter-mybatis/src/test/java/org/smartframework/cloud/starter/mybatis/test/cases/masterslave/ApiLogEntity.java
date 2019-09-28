package org.smartframework.cloud.starter.mybatis.test.cases.masterslave;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "t_api_log")
public class ApiLogEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 接口描述 */
	@Column(name = "f_api_desc")
	private String apiDesc;

	/** 调用的类方法 */
	@Column(name = "f_class_method")
	private String classMethod;

	/** 接口url */
	@Column(name = "f_url")
	private String url;

	/** http请求方式 */
	@Column(name = "f_http_method")
	private String httpMethod;

	/** 客户端ip */
	@Column(name = "f_ip")
	private String ip;

	/** 操作系统相关信息 */
	@Column(name = "f_os")
	private String os;

	/** 异常堆栈信息 */
	@Column(name = "f_exception_stack_info")
	private String exceptionStackInfo;

	/** 请求开始时间 */
	@Column(name = "f_req_start_time")
	private Date reqStartTime;

	/** 请求截止时间 */
	@Column(name = "f_req_end_time")
	private Date reqEndTime;

	/** 请求处理时间（毫秒） */
	@Column(name = "f_req_deal_time")
	private Integer reqDealTime;

	/** 请求的参数信息 */
	@Column(name = "f_req_params")
	private String reqParams;

	/** 响应的数据 */
	@Column(name = "f_resp_data")
	private String respData;

}