package ${packageName};

<#list importPackages as package>
import ${package};
</#list>
import org.smartframework.cloud.common.pojo.vo.BaseEntityRespVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * ${tableComment}
 *
 * @author ${classComment.author}
 * @date ${classComment.createDate}
 */
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class ${className} extends BaseEntityRespVO {

	private static final long serialVersionUID = 1L;

<#list attributes as attribute>
	<#if attribute.comment?? && attribute.comment!="">
    /** ${attribute.comment} */
	</#if>
	<#if attribute.maskRule??>
    @MaskLog(MaskRule.${attribute.maskRule})
	</#if>
	private ${attribute.javaType} ${attribute.name};
	
</#list>
}