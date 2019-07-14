package org.smartframework.cloud.code.generate.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.dto.template.BaseMapperDto;
import org.smartframework.cloud.code.generate.dto.template.BaseRespBodyDto;
import org.smartframework.cloud.code.generate.dto.template.EntityDto;

import lombok.experimental.UtilityClass;

/**
 * 代码文件生成工具类
 *
 * @author liyulin
 * @date 2019-07-15
 */
@UtilityClass
public class CodeFileGenerateUtil {

	private static final String SRC_MAIN_JAVA = "/src/main/java/";
	private static final String JAVA_FILE_SUFFIX = ".java";

	public static void generateBaseMapper(BaseMapperDto baseMapperDto, String basePath) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(baseMapperDto, Config.Template.BASE_MAPPER);
		String filePath = getClassFilePath(basePath, baseMapperDto.getPackageName(), baseMapperDto.getClassName());
		FileUtils.writeStringToFile(new File(filePath), code, StandardCharsets.UTF_8.name());
	}

	public static void generateEntity(EntityDto entityDto, String basePath) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(entityDto, Config.Template.ENTITY);
		String filePath = getClassFilePath(basePath, entityDto.getPackageName(), entityDto.getClassName());
		FileUtils.writeStringToFile(new File(filePath), code, StandardCharsets.UTF_8.name());
	}

	public static void generateBaseRespBody(BaseRespBodyDto baseRespBodyDto, String basePath) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(baseRespBodyDto, Config.Template.BASE_RESPBODY);
		String filePath = getClassFilePath(basePath, baseRespBodyDto.getPackageName(), baseRespBodyDto.getClassName());

		FileUtils.writeStringToFile(new File(filePath), code, StandardCharsets.UTF_8.name());
	}

	private static String getClassFilePath(String basePath, String classPackage, String className) {
		return basePath + SRC_MAIN_JAVA + classPackage.replaceAll("\\.", "/") + "/" + className + JAVA_FILE_SUFFIX;
	}

}