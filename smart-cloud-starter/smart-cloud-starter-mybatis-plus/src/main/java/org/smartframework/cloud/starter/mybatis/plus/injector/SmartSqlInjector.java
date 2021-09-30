package org.smartframework.cloud.starter.mybatis.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.smartframework.cloud.starter.mybatis.plus.injector.methods.Truncate;

import java.util.List;

/**
 * smart cloud sql注入器
 *
 * @author collin
 * @date 2021-03-23
 */
public class SmartSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        // 添加in-line式批量插入
        methodList.add(new InsertBatchSomeColumn());
        methodList.add(new Truncate());
        return methodList;
    }

}