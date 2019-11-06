package org.smartframework.cloud.starter.log;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.smartframework.cloud.mask.util.MaskUtil;

/**
 * @desc mask MessageFactory
 * @author liyulin
 * @date 2019/11/06
 */
public class MaskMessageFactory extends AbstractMessageFactory {

	private static final long serialVersionUID = 1L;

	@Override
	public Message newMessage(String message, Object... params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				params[i] = MaskUtil.mask(params[i]);
			}
		}
		return new SimpleMessage(ParameterizedMessage.format(message, params));
	}

}