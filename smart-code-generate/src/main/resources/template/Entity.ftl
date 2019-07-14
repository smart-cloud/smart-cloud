package ${packageName};

import javax.persistence.Column;
import javax.persistence.Table;

import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * ${tableComment}
 *
 * @author ${common.author}
 * @date ${common.createDate}
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "${tableName}")
public class ${className} extends BaseEntity {

	private static final long serialVersionUID = 1L;

<#list attributes as attribute>
	<#if attribute.comment!="">
    /** ${attribute.comment} */
	</#if>
    @Column(name = "${attribute.name}")     
	private ${attribute.javaType} ${attribute.name};
	
</#list>
	/** 表字段名 */
	public enum Columns {
		<#list attributes as attribute>
		<#if attribute.comment!="">
	    /** ${attribute.comment} */
		</#if>
		${attribute.name}<#if attribute_has_next>,<#else>;</#if>
		</#list>
	}

}