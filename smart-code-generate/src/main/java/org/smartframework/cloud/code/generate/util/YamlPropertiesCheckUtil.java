/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartframework.cloud.code.generate.util;

import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;
import org.smartframework.cloud.code.generate.properties.CodeProperties;
import org.smartframework.cloud.code.generate.properties.DbProperties;
import org.smartframework.cloud.code.generate.properties.YamlProperties;
import org.smartframework.cloud.mask.MaskRule;

import java.util.Map;

/**
 * @author collin
 * @desc YamlProperties校验工具类
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
        if (isCodeSpecifiedTablesEmpty(codeProperties)) {
            throw new IllegalArgumentException("code.specifiedTables未配置");
        }

        // mask
        checkMask(codeProperties.getMask());

        // mainClassPackage
        if (isBlank(codeProperties.getMainClassPackage())) {
            throw new IllegalArgumentException("code.mainClassPackage未配置");
        }
    }

    private static boolean isCodeSpecifiedTablesEmpty(CodeProperties codeProperties) {
        return (codeProperties.getType().compareTo(GenerateTypeEnum.EXCLUDE.getType()) == 0
                || codeProperties.getType().compareTo(GenerateTypeEnum.INCLUDE.getType()) == 0)
                && isBlank(codeProperties.getSpecifiedTables());
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
            maskRuleMap.forEach((column, maskRule) -> checkMaskRule(maskRule));
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