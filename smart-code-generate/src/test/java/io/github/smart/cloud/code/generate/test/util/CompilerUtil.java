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
package io.github.smart.cloud.code.generate.test.util;

import javax.tools.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * class编译工具类
 *
 * @author collin
 * @date 2021-12-12
 */
public class CompilerUtil {

    /**
     * class编译
     *
     * @param names class类路径
     * @return 编译错误信息
     */
    public static List<? super JavaFileObject> compile(Iterable<String> names) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(names);
        DiagnosticCollector<? super JavaFileObject> diagnosticCollector = new DiagnosticCollector();
        compiler.getTask(null, fileManager, diagnosticCollector, null, null, compilationUnits).call();
        return diagnosticCollector.getDiagnostics()
                .stream()
                .filter(item -> item.getKind() == Diagnostic.Kind.ERROR)
                .collect(Collectors.toList());
    }

}