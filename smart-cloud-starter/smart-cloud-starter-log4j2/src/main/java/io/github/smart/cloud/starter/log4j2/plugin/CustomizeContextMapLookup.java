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
package io.github.smart.cloud.starter.log4j2.plugin;

import io.github.smart.cloud.starter.log4j2.constants.LogConstants;
import io.github.smart.cloud.starter.log4j2.enums.ExtProperty;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;
import io.github.smart.cloud.starter.log4j2.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义lo4j2插件，用于设置变量
 *
 * <p>
 * NOTE：需要在log4j2的配置文件中设置“packages”的属性值（即当前类的包名）
 *
 * @author collin
 * @date 2019-03-19
 */
@Plugin(name = "cctx", category = StrLookup.CATEGORY)
public class CustomizeContextMapLookup implements StrLookup {

    /**
     * yaml文件名
     */
    private static final String YAML_FILE_NAME = "application.yaml";
    /**
     * spring yaml文件中key的分隔符
     */
    private static final String SPRING_YAML_KEY_SEPARATOR = "\\.";
    /**
     * yaml文件中的应用名key
     */
    private static final String APP_NAME_KEY = "spring.application.name";
    /**
     * windows系统标签
     */
    private static final String WINDOWS_TAG = "windows";
    /**
     * 操作系统名称属性
     */
    private static final String OS_NAME = "os.name";
    /**
     * 存储设置的变量
     */
    private static final Map<String, String> DATA = new HashMap<>(2);

    static {
        DATA.putAll(init(YAML_FILE_NAME));
    }

    public static Map<String, String> init(String yamlFileName) {
        // 解析yaml
        ClassPathResource resource = new ClassPathResource(yamlFileName);
        String appName = null;
        if (!resource.exists()) {
            // 不存在，则取当前工程名
            appName = getCurrentProjectName();
        } else {
            appName = readAppNameFromYaml(resource);
            if (appName == null || appName.length() == 0) {
                appName = getCurrentProjectName();
            }
        }

        Map<String, String> data = new HashMap<>(2);
        data.put(ExtProperty.APP_NAME.getName(), appName);
        data.put(ExtProperty.LOG_PATH.getName(), getLogPath(appName));

        return data;
    }

    private static String getLogPath(String appName) {
        // 当前为windows系统
        if (System.getProperty(OS_NAME).toLowerCase().indexOf(WINDOWS_TAG) != -1) {
            return String.format("%s%s", LogConstants.WINDOWS_LOG_DIR, appName);
        }
        return String.format("%s%s", LogConstants.LINUX_LOG_DIR, appName);
    }

    /**
     * 获取当前工程名
     *
     * @return
     */
    private static String getCurrentProjectName() {
        ApplicationHome applicationHome = new ApplicationHome(CustomizeContextMapLookup.class);
        File source = getApplicationHomeSource(applicationHome);
        if (source == null) {
            // 外部工程
            File dir = applicationHome.getDir();
            if (dir == null) {
                return null;
            }
            return dir.getName();
        }

        // 本工程
        return getProjectNameFromApplicationHomeSource(source);
    }

    /**
     * 通过放射获取{@link ApplicationHome}的属性source
     *
     * @param applicationHome
     * @return
     */
    private static File getApplicationHomeSource(ApplicationHome applicationHome) {
        File source = null;
        try {
            Field field = ApplicationHome.class.getDeclaredField("source");
            field.setAccessible(true);
            source = (File) field.get(applicationHome);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return source;
    }

    /**
     * 从{@link ApplicationHome}的属性source中获取工程名
     *
     * @param source
     * @return
     */
    private static String getProjectNameFromApplicationHomeSource(File source) {
        String endWord = File.separator + "target" + File.separator;
        String path = source.getPath();
        String tmp = path.substring(0, path.indexOf(endWord));
        return tmp.substring(tmp.lastIndexOf(File.separator) + 1);
    }

    /**
     * 从yaml文件中读取服务名
     *
     * @param resource
     * @return
     */
    private static String readAppNameFromYaml(ClassPathResource resource) {
        Yaml yaml = new Yaml();
        String appName = null;
        try (InputStream yamlInputStream = resource.getInputStream()) {
            Map<String, Object> yamlJson = yaml.load(yamlInputStream);
            appName = getYamlValue(APP_NAME_KEY, yamlJson);
        } catch (IOException e) {
            // 抛异常，则取当前jar名
            e.printStackTrace();
        }
        return appName;
    }

    @Override
    public String lookup(String key) {
        return DATA.get(key);
    }

    @Override
    public String lookup(LogEvent event, String key) {
        return DATA.get(key);
    }

    /**
     * 解析yaml文件中的json
     *
     * @param name
     * @param yamlJson
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String getYamlValue(String name, Map<String, Object> yamlJson) {
        if (yamlJson == null) {
            return null;
        }
        String[] keys = name.split(SPRING_YAML_KEY_SEPARATOR);
        if (Objects.isNull(keys) || keys.length == 0) {
            return null;
        }

        if (keys.length == 1) {
            return String.valueOf(yamlJson.get(keys[0]));
        }

        Map<String, Object> tempMap = null;
        String value = null;
        for (int i = 0; i < keys.length; i++) {
            if (i == 0) {
                Object tmp = yamlJson.get(keys[i]);
                if (tmp == null) {
                    return null;
                }
                tempMap = (Map<String, Object>) tmp;
            } else if (i < keys.length - 1) {
                Object tmp = tempMap.get(keys[i]);
                if (tmp == null) {
                    return null;
                }
                tempMap = (Map<String, Object>) tmp;
            } else {
                value = String.valueOf(tempMap.get(keys[i]));
            }
        }

        return value;
    }

}