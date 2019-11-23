package ${packageName};

<#list importPackages as package>
import ${package};
</#list>
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.ExtMapper;

/**
 * ${tableComment}base mapper
 *
 * @author ${classComment.author}
 * @date ${classComment.createDate}
 */
public interface ${className} extends ExtMapper<${entityClassName}, ${baseRespBodyClassName}, Long> {

}