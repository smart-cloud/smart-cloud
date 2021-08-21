package org.smartframework.cloud.starter.mybatis.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * smart cloud sql注入器
 *
 * @author collin
 * @date 2021-03-23
 */
public class SmartSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        // 添加in-line式批量插入
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }

}