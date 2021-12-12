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

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.smartframework.cloud.code.generate.config.Config;

import java.io.StringWriter;
import java.io.Writer;

/**
 * FreeMarker 工具类
 *
 * @author collin
 * @date 2019-07-13
 */
public class FreeMarkerUtil {

    private static Configuration freemarkerCfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    static {
        freemarkerCfg.setClassForTemplateLoading(FreeMarkerUtil.class, Config.TEMPLATE_PATH);
    }

    private FreeMarkerUtil() {
    }

    /**
     * 根据模板名及模板参数信息，获取渲染后的字符串
     *
     * @param data
     * @param templateName
     * @return
     */
    public static String freeMarkerRender(Object data, String templateName) throws Exception {
        try (Writer out = new StringWriter();) {
            Template template = freemarkerCfg.getTemplate(templateName, Config.DEFAULT_ENCODING);
            template.process(data, out);
            out.flush();
            return out.toString();
        }
    }

}