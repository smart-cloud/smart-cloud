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
package org.smartframework.cloud.starter.locale.constant;

/**
 * locale常量
 *
 * @author collin
 * @date 2019-07-15
 */
public interface LocaleConstant {

    /**
     * locale文件后缀
     */
    String LOCALE_PROPERTIES_SUFFIX = ".properties";
    /**
     * locale文件目录
     */
    String LOCALE_DIR = "i18n/";
    /**
     * locale文件路径
     */
    String LOCALE_PATTERN = "classpath*:/" + LOCALE_DIR + "*messages.properties";

}