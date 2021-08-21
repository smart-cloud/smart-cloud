package ${packageName};

import org.apache.ibatis.annotations.Mapper;
<#list importPackages as package>
import ${package};
</#list>
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.SmartMapper;

/**
 * ${tableComment}base mapper
 *
 * @author ${classComment.author}
 * @date ${classComment.createDate}
 */
@Mapper
public interface ${className} extends SmartMapper<${entityClassName}> {

}