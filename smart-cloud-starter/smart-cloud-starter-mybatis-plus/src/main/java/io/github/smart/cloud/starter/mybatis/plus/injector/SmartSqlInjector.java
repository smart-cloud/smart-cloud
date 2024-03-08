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
package io.github.smart.cloud.starter.mybatis.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import io.github.smart.cloud.starter.mybatis.plus.injector.methods.IsExist;
import io.github.smart.cloud.starter.mybatis.plus.injector.methods.Truncate;

import java.util.List;

/**
 * smart cloud sql注入器
 *
 * @author collin
 * @date 2021-03-23
 */
public class SmartSqlInjector extends DefaultSqlInjector {

    /**
     * truncate操作对应的方法名称
     */
    private static final String TRUNCATE_METHOD_NAME = "truncate";

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        // 添加in-line式批量插入
        methodList.add(new InsertBatchSomeColumn());
        methodList.add(new Truncate());
        methodList.add(new IsExist());
        return methodList;
    }

}