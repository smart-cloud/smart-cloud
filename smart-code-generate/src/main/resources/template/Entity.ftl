package ${packageName};

import javax.persistence.Column;
import javax.persistence.Table;

import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * ${tableComment}
 *
 * @author ${author}
 * @date ${createDate}
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "${tableName}")
public class ${entityClassName} extends BaseEntity {

	private static final long serialVersionUID = 1L;

<#list attributes as attribute>
	<#if attribute.comment??>
    /** ${attribute.comment} */
	</#if>
    @Column(name = "${attribute.column.name}")     
	private ${attribute.type} ${attribute.name};
	
</#list>

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public enum Columns {
		<#list attributes as attribute>
		<#if attribute.comment??>
	    /** ${attribute.comment} */
		</#if>
		${attribute.name},
		</#list>
	}

}