package ${packageName};

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
<#list importPackages as package>
import ${package};
</#list>

/**
 * ${tableComment}base mapper
 *
 * @author ${classComment.author}
 * @date ${classComment.createDate}
 */
<#if dsValue!="">
@DS(${dsValue})
</#if>
@Mapper
public interface ${className} extends BaseMapper<${entityClassName}> {

}