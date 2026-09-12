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
package io.github.smart.cloud.code.generate.core.impl;

import io.github.smart.cloud.code.generate.bo.template.buildparam.TemplateBuildParamContext;
import io.github.smart.cloud.code.generate.bo.template.param.CommonBO;
import io.github.smart.cloud.code.generate.config.Config;
import io.github.smart.cloud.code.generate.core.ISourceFileGenerateStrategy;
import io.github.smart.cloud.code.generate.enums.FileType;
import io.github.smart.cloud.code.generate.util.FreeMarkerUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 生成源代码文件策略抽象类
 *
 * @author collin
 * @date 2025-04-29
 */
public abstract class AbstractSourceFileGenerateStrategy<T extends CommonBO> implements ISourceFileGenerateStrategy {

    /**
     * 构建模板参数
     *
     * @param context
     * @return
     */
    protected abstract T buildTemplateParams(TemplateBuildParamContext context);

    /**
     * 获取文件base路径
     *
     * @param context
     * @return
     */
    protected abstract String getBasePath(TemplateBuildParamContext context);

    @Override
    public void generateSourceFile(TemplateBuildParamContext context) throws Exception {
        T templateParams = buildTemplateParams(context);
        String basePath = getBasePath(context);
        FileType fileType = getFileType();

        String newCode = FreeMarkerUtil.freeMarkerRender(templateParams, fileType.getTemplateFilename());
        String filePath = getClassFilePath(basePath, templateParams.getPackageName(), templateParams.getClassName());

        File codeFile = new File(filePath);
        boolean override = isOverride(codeFile, newCode);
        if (override) {
            System.out.println(filePath);
            FileUtils.writeStringToFile(codeFile, newCode, Config.DEFAULT_ENCODING);
        } else {
            System.out.printf("[%s]生成内容与原有内容相同，不覆盖！！！%n", codeFile.getName());
        }
    }

    /**
     * 是否需要覆盖原有的文件（会去除@date后比较）
     *
     * @param codeFile 代码文件
     * @param newCode  新生成的代码内容
     * @return
     * @throws IOException
     */
    private static boolean isOverride(File codeFile, String newCode) throws IOException {
        if (!codeFile.exists()) {
            return true;
        }

        String oldCode = FileUtils.readFileToString(codeFile, Config.DEFAULT_ENCODING);
        String oldCodeAfterRemoveDate = getCodeAfterRemoveDate(oldCode);
        String newCodeAfterRemoveDate = getCodeAfterRemoveDate(newCode);

        return !Objects.equals(newCodeAfterRemoveDate, oldCodeAfterRemoveDate);
    }

    /**
     * 获取移除@date后的code内容
     *
     * @param code
     * @return
     */
    private static String getCodeAfterRemoveDate(String code) {
        int codeDateIndex = code.indexOf(Config.DATE_ANNOTATION_TAG);
        String codeAfterRemoveDate = null;
        if (codeDateIndex != -1) {
            // 需要移除的字符串长度
            int removeLength = Config.DATE_ANNOTATION_TAG.length() + Config.CODE_CREATE_DATE_FORMAT.length() + 1;
            codeAfterRemoveDate = code.substring(0, codeDateIndex)
                    + code.substring(codeDateIndex + removeLength);
        } else {
            codeAfterRemoveDate = code;
        }
        return codeAfterRemoveDate;
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
        return Paths.get(basePath, Config.SRC_MAIN_JAVA,
                        classPackage.replace('.', File.separatorChar),
                        className + Config.JAVA_FILE_SUFFIX)
                .normalize().toString();
    }

}