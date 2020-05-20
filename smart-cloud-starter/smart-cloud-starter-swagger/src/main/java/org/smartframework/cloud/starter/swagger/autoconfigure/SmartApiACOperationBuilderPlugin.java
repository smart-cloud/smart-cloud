package org.smartframework.cloud.starter.swagger.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.api.core.annotation.SmartApiAC;
import org.smartframework.cloud.api.core.enums.SignType;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

/**
 * {@link SmartApiAC}文档处理
 *
 * @author liyulin
 * @date 2020-05-21
 */
@Configuration
public class SmartApiACOperationBuilderPlugin implements OperationBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(OperationContext context) {
		List<SmartApiAC> smartApiACList = context.findAllAnnotations(SmartApiAC.class);
		if (CollectionUtils.isNotEmpty(smartApiACList)) {
			String notes = buildNotes(smartApiACList.get(0));
			if (StringUtils.isNoneBlank(notes)) {
				context.operationBuilder().notes(notes);
			}
		}
	}

	private String buildNotes(SmartApiAC smartApiAC) {
		List<String> noteList = buildNoteList(smartApiAC);

		return convertListToString(noteList);
	}

	private String convertListToString(List<String> noteList) {
		if (CollectionUtils.isEmpty(noteList)) {
			return null;
		}
		if (noteList.size() == 1) {
			return noteList.get(0);
		}

		StringBuilder notes = new StringBuilder(64);
		for (int i = 0; i < noteList.size(); i++) {
			notes.append((i + 1) + "、" + noteList.get(i)).append("<br>");
		}

		return notes.toString();
	}

	private List<String> buildNoteList(SmartApiAC smartApiAC) {
		List<String> noteList = new ArrayList<>();
		if (smartApiAC.tokenCheck()) {
			noteList.add("需要token校验");
		}
		SignType signType = smartApiAC.sign();
		if (signType != SignType.NONE) {
			noteList.add(getSignTypeDesc(signType));
		}
		if (smartApiAC.decrypt()) {
			noteList.add("请求参数需要加密");
		}
		if (smartApiAC.encrypt()) {
			noteList.add("响应信息需要解密");
		}
		if (smartApiAC.auth()) {
			noteList.add("接口需要鉴权");
		}

		return noteList;
	}

	private String getSignTypeDesc(SignType signType) {
		switch (signType) {
		case REQUEST:
			return "只有请求参数需要签名 ";
		case RESPONSE:
			return "只有响应参数需要验签";
		case ALL:
			return "请求参数需要签名，且响应参数需要验签";
		default:
			break;
		}
		return "";
	}

}
