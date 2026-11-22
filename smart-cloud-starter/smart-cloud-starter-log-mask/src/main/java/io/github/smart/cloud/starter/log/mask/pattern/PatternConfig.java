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
package io.github.smart.cloud.starter.log.mask.pattern;

import io.github.smart.cloud.starter.log.mask.pattern.enums.MaskMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * 正则配置
 *
 * @author collin.li
 * @date 2026-03-01
 */
public class PatternConfig {

    /**
     * 脱敏模式
     *
     * @see MaskMode
     */
    @Setter
    @Getter
    private static Integer mode;

    /**
     * 用于解析键值对（Key-Value Pairs）的正则表达式模式。
     * 该模式设计用于匹配诸如 {@code name = "张三"}、{@code age:30} 或 {@code 城市="北京"} 这样的字符串，
     * 并支持简单的引号包装和转义。
     *
     * <p>表达式结构分解：
     * <ol>
     *   <li><b>键（Key）</b>：第一个捕获组 {@code $1}。由单词字符（包括中文字符）构成，可选择性地被单引号、双引号或转义引号包装。</li>
     *   <li><b>分隔符</b>：第二个捕获组 {@code $2}。允许使用中文冒号（：）、英文冒号（:）或等号（=），分隔符前后可存在空白。</li>
     *   <li><b>值（Value）</b>：第三个捕获组 {@code $3}。匹配直到遇到指定分隔符（如逗号、分号、方括号等）或空白前的字符序列（引号、转义引号包含在内）。</li>
     * </ol>
     *
     * <p><b>特性与限制：</b>
     * <ul>
     *   <li>支持中英文键名和值。</li>
     *   <li>支持简单的引号转义（例如：{@code \"}）。</li>
     *   <li>使用占有型量词（{@code *+}, {@code ++}）以优化性能，避免回溯。</li>
     *   <li>键首字符前使用否定后顾 {@code (?<![\w\u4e00-\u9fa5])}，使 {@code find()} 从单词中间开始的尝试立即失败，避免长单词导致的 O(n²) 扫描。</li>
     *   <li>匹配不包含键前、值后的空白，替换时空白原样保留。</li>
     *   <li>值部分不能包含常见的配置项分隔符（如 {@code ,}、{@code ;}、{@code []}、{@code {}} 和空白字符），这通常意味着它适合解析简单的、由这些符号分隔的键值对列表。</li>
     * </ul>
     */
    public static final Pattern REGEX_PATTERN = Pattern.compile("((?:\\\\\"|[\"'])?+(?<![\\w\\u4e00-\\u9fa5])[\\w\\u4e00-\\u9fa5]++(?:\\\\\"|[\"'])?+)(\\s*+[：:=]\\s*+)([^,\\[\\]{};，；\\s]++)");
    /**
     * 正则匹配的字段名
     */
    @Getter
    private static Set<String> fieldNameSet = new HashSet<>();
    /**
     * 默认的脱敏字段
     */
    private static String[] DEFAULT_FIELD_NAMES = new String[]{
            // 地址
            "address", "地址", "家庭地址", "工作地址",
            // 姓名
            "userName", "englishName", "chineseName", "realName", "firstName", "lastName",
            "fullName", "nickName", "姓名", "用户名",
            // 身份证
            "idCard", "idNumber", "身份证",
            // 密码
            "secretKey", "password", "密码", "passwd",
            // 验证码
            "smsCode", "verifyCode", "captcha", "验证码",
            // 银行卡
            "bankCard", "银行卡",
            // 邮箱
            "email", "邮箱",
            // 手机号
            "phone", "mobile", "tel", "手机号"};
    private static final ConcurrentMap<String, Pattern> MASK_REGEX_CACHE = new ConcurrentHashMap<>();

    /**
     * 正则表达式转换
     *
     * @param regex
     */
    public static Pattern convert(String regex) {
        if ("".equals(regex)) {
            return REGEX_PATTERN;
        }

        return MASK_REGEX_CACHE.computeIfAbsent(regex, key -> Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
    }

    /**
     * 重置脱敏自带怒
     *
     * @param names
     */
    public static void resetMaskFieldSet(Set<String> names) {
        fieldNameSet.clear();
        // 字段前后缀：兼容带单引号、双引号、带反斜杠+双引号
        String[] wraps = {"", "'", "\"", "\\\""};
        for (String wrap : wraps) {
            for (String name : DEFAULT_FIELD_NAMES) {
                fieldNameSet.add(wrap + name.toLowerCase() + wrap);
            }
            for (String name : names) {
                fieldNameSet.add(wrap + name.toLowerCase() + wrap);
            }
        }
    }

}