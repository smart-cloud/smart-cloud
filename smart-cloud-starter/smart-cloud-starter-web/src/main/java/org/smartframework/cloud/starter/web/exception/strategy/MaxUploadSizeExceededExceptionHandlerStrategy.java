package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.smartframework.cloud.utility.spring.I18NUtil;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @desc 上传文件大小超过限制异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class MaxUploadSizeExceededExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof MaxUploadSizeExceededException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		return RespHeadUtil.of(ReturnCodeEnum.UPLOAD_FILE_SIZE_EXCEEDED, I18NUtil.getMessage(e.getMessage()));
	}
	
}