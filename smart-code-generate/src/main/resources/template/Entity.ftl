package ${packageName};

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
<#list importPackages as package>
import ${package};
</#list>
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.entity.BaseEntity;

/**
 * ${tableComment}
 *
 * @author ${classComment.author}
 * @date ${classComment.createDate}
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@TableName("${tableName}")
public class ${className} extends BaseEntity {

	private static final long serialVersionUID = 1L;

<#list attributes as attribute>
	<#if attribute.comment?? && attribute.comment!="">
    /** ${attribute.comment} */
	</#if>
	<#if attribute.maskRule??>
    @MaskLog(MaskRule.${attribute.maskRule})
	</#if>
    <#if attribute.primaryKey>
    @TableId(value = "${attribute.columnName}")
    <#else>
    @TableField(value = "${attribute.columnName}")
    </#if>
	private ${attribute.javaType} ${attribute.name};
	
</#list>
}