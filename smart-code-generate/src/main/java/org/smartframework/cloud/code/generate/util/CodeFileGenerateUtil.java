package org.smartframework.cloud.code.generate.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.dto.template.BaseMapperDto;
import org.smartframework.cloud.code.generate.dto.template.BaseRespBodyDto;
import org.smartframework.cloud.code.generate.dto.template.EntityDto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码文件生成工具类
 *
 * @author liyulin
 * @date 2019-07-15
 */
@UtilityClass
@Slf4j
public class CodeFileGenerateUtil {

	/** maven工程源码目录 */
	private static final String SRC_MAIN_JAVA = "/src/main/java/";
	/** java文件名后缀 */
	private static final String JAVA_FILE_SUFFIX = ".java";

	/**
	 * 生成Mapper
	 * 
	 * @param baseMapperDto
	 * @param basePath
	 * @throws IOException
	 */
	public static void generateBaseMapper(BaseMapperDto baseMapperDto, String basePath) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(baseMapperDto, Config.Template.BASE_MAPPER);
		String filePath = getClassFilePath(basePath, baseMapperDto.getPackageName(), baseMapperDto.getClassName());
		log.info(filePath);

		FileUtils.writeStringToFile(new File(filePath), code, Config.DEFAULT_ENCODING);
	}

	/**
	 * 生成Entity
	 * 
	 * @param entityDto
	 * @param basePath
	 * @throws IOException
	 */
	public static void generateEntity(EntityDto entityDto, String basePath) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(entityDto, Config.Template.ENTITY);
		String filePath = getClassFilePath(basePath, entityDto.getPackageName(), entityDto.getClassName());
		log.info(filePath);

		FileUtils.writeStringToFile(new File(filePath), code, Config.DEFAULT_ENCODING);
	}

	/**
	 * 生成RespBody
	 * 
	 * @param baseRespBodyDto
	 * @param basePath
	 * @throws IOException
	 */
	public static void generateBaseRespBody(BaseRespBodyDto baseRespBodyDto, String basePath) throws IOException {
		String code = FreeMarkerUtil.freeMarkerRender(baseRespBodyDto, Config.Template.BASE_RESPBODY);
		String filePath = getClassFilePath(basePath, baseRespBodyDto.getPackageName(), baseRespBodyDto.getClassName());
		log.info(filePath);

		FileUtils.writeStringToFile(new File(filePath), code, Config.DEFAULT_ENCODING);
	}

	/**
	 * 获取类文件路径
	 * 
	 * @param basePath
	 * @param classPackage
	 * @param className
	 * @return
	 */
	private static String getClassFilePath(String basePath, String classPackage, String className) {
		return basePath + SRC_MAIN_JAVA + classPackage.replaceAll("\\.", "/") + "/" + className + JAVA_FILE_SUFFIX;
	}

}