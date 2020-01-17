package ${packageName};

<#list importPackages as package>
import ${package};
</#list>
import org.smartframework.cloud.common.pojo.vo.BaseEntityRespVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@ApiModel(description = "${tableComment}")
<#if enableMask??>
@${enableMask}
</#if>
public class ${className} extends BaseEntityRespVO {

	private static final long serialVersionUID = 1L;

<#list attributes as attribute>
	<#if attribute.comment!="">
    @ApiModelProperty(value = "${attribute.comment}")
	</#if>
	<#if attribute.maskRule??>
    @MaskLog(MaskRule.${attribute.maskRule})
	</#if>
	private ${attribute.javaType} ${attribute.name};
	
</#list>
}