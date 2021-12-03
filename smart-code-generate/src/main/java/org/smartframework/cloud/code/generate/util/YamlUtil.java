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

import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.properties.YamlProperties;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * yaml读取工具类
 *
 * @author liyulin
 * @date 2019-07-15
 */
public final class YamlUtil {

    private YamlUtil() {
    }

    /**
     * 从yaml文件去读配置信息
     *
     * @return
     */
    public static YamlProperties readYamlProperties() throws IOException {
        // 获取yaml文件路径
        ClassPathResource resource = new ClassPathResource(getYamlPath());
        // 创建Yaml对象
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(representer);
        // 读取yaml文件信息
        YamlProperties yamlProperties = null;
        try (InputStream yamlInputStream = resource.getInputStream()) {
            yamlProperties = yaml.loadAs(yamlInputStream, YamlProperties.class);
        }
        System.out.println(String.format("yamlProperties==>%s", yamlProperties));
        // 换行
        System.out.println();

        return yamlProperties;
    }

    /**
     * 从properties文件获取配置的yaml文件路径
     *
     * @return
     */
    private static String getYamlPath() {
        ResourceBundle configResource = ResourceBundle.getBundle(Config.CONFIG_NAME);
        String configPath = configResource.getString(Config.PROPERTIES_KEY);
        if (!configPath.endsWith(YAML_SUFFIX)) {
            configPath = configPath + YAML_SUFFIX;
        }
        return Config.CONFIG_PATH + configPath;
    }

    /**
     * yaml文件后缀
     */
    private static final String YAML_SUFFIX = ".yaml";

}