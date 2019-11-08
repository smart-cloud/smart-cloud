package org.smartframework.cloud.code.generate.util;

import java.util.Map;

import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.DbProperties;
import org.smartframework.cloud.code.generate.properties.PathProperties;
import org.smartframework.cloud.code.generate.properties.ProjectProperties;
import org.smartframework.cloud.code.generate.properties.YamlProperties;
import org.smartframework.cloud.mask.MaskRule;

/**
 * @desc YamlProperties校验工具类
 * @author liyulin
 * @date 2019/11/08
 */
public class YamlPropertiesCheckUtil {

	private YamlPropertiesCheckUtil() {
	}

	/**
	 * 校验{@link YamlProperties}属性值
	 * 
	 * @param yamlProperties
	 */
	public static void check(YamlProperties yamlProperties) {
		if (yamlProperties == null) {
			throw new IllegalArgumentException("yamlProperties不能为null（yaml文件读取失败）");
		}
		checkDbProperties(yamlProperties.getDb());
		checkCodeProperties(yamlProperties.getCode());
	}

	/**
	 * db属性校验
	 * 
	 * @param dbProperties
	 */
	private static void checkDbProperties(DbProperties dbProperties) {
		if (dbProperties == null) {
			throw new IllegalArgumentException("db不能为null（db属性未配置）");
		}

		if (isBlank(dbProperties.getUrl())) {
			throw new IllegalArgumentException("db.url未配置");
		}

		if (isBlank(dbProperties.getUsername())) {
			throw new IllegalArgumentException("db.username未配置");
		}

		if (isBlank(dbProperties.getPassword())) {
			throw new IllegalArgumentException("db.password未配置");
		}
	}

	/**
	 * code属性校验
	 * 
	 * @param codeProperties
	 */
	private static void checkCodeProperties(CodeProperties codeProperties) {
		if (codeProperties == null) {
			throw new IllegalArgumentException("code不能为null（code属性未配置）");
		}

		// author
		if (isBlank(codeProperties.getAuthor())) {
			throw new IllegalArgumentException("code.author未配置");
		}

		// type
		checkGenerateType(codeProperties.getType());

		// specifiedTables
		if ((codeProperties.getType().compareTo(GenerateTypeEnum.EXCLUDE.getType()) == 0
				|| codeProperties.getType().compareTo(GenerateTypeEnum.INCLUDE.getType()) == 0)
				&& isBlank(codeProperties.getSpecifiedTables())) {
			throw new IllegalArgumentException("code.specifiedTables未配置");
		}

		// mask
		checkMask(codeProperties.getMask());

		// mainClassPackage
		if (isBlank(codeProperties.getMainClassPackage())) {
			throw new IllegalArgumentException("code.mainClassPackage未配置");
		}

		// project
		checkProject(codeProperties.getProject());
	}

	private static void checkGenerateType(Integer generateType) {
		if (generateType == null) {
			throw new IllegalArgumentException("code.type未配置");
		}
		// 值合法性校验
		GenerateTypeEnum[] typeEnums = GenerateTypeEnum.values();
		for (GenerateTypeEnum typeEnum : typeEnums) {
			if (typeEnum.getType().compareTo(generateType) == 0) {
				return;
			}
		}

		throw new IllegalArgumentException("code.type值不合法");
	}

	private static void checkMask(Map<String, Map<String, String>> mask) {
		if (mask == null) {
			return;
		}

		mask.forEach((tableName, maskRuleMap) -> {
			if (maskRuleMap == null || maskRuleMap.isEmpty()) {
				throw new IllegalArgumentException("code.mask表字段mask规则未配置");
			}
			maskRuleMap.forEach((column, maskRule) -> {
				checkMaskRule(maskRule);
			});
		});
	}

	/**
	 * 校验mask规则是否合法
	 * 
	 * @param maskRule
	 */
	private static void checkMaskRule(String maskRule) {
		MaskRule[] maskRules = MaskRule.values();
		for (MaskRule maskRuleTemp : maskRules) {
			if (maskRuleTemp.toString().equals(maskRule)) {
				return;
			}
		}

		throw new IllegalArgumentException(String.format("不支持的mask规则【%s】", maskRule));
	}

	private static void checkProject(ProjectProperties project) {
		if (project == null) {
			throw new IllegalArgumentException("code.project未配置");
		}

		PathProperties pathProperties = project.getPath();
		if (pathProperties == null) {
			throw new IllegalArgumentException("code.project.path未配置");
		}

		if (isBlank(pathProperties.getRpc())) {
			throw new IllegalArgumentException("code.project.path.rpc未配置");
		}

		if (isBlank(pathProperties.getService())) {
			throw new IllegalArgumentException("code.project.path.service未配置");
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	private static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}
}