package ${packageName};

import org.smartframework.cloud.common.pojo.dto.BaseEntityRespBody;

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
public class ${className} extends BaseEntityRespBody {

	private static final long serialVersionUID = 1L;

<#list attributes as attribute>
	<#if attribute.comment!="">
    @ApiModelProperty(value = "${attribute.comment}")
	</#if>
    @Column(name = "${attribute.name}")     
	private ${attribute.javaType} ${attribute.name};
	
</#list>
}