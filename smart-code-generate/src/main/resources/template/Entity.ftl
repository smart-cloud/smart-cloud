/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import io.github.smart.cloud.starter.mybatis.plus.common.entity.BaseEntity;

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