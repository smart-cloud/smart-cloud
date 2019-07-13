package org.smartframework.cloud.starter.mybatis.common.mapper.ext.provider;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 批量更新
 *
 * @author liyulin
 * @date 2019-03-24
 */
public class BatchUpdateMapperProvider extends MapperTemplate {

	public BatchUpdateMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 通过主键更新全部字段
	 *
	 * @param ms
	 */
	public String updateListByPrimaryKey(MappedStatement ms) {
		String entityName = "item";
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder(64);
		sql.append("<foreach item=\"" + entityName + "\" collection=\"list\" separator=\";\">");
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.updateSetColumns(entityClass, entityName, false, false));
		sql.append(SqlHelper.wherePKColumns(entityClass, entityName, true));
		sql.append("</foreach>");
		return sql.toString();
	}

	/**
	 * 通过主键更新不为null的字段
	 *
	 * @param ms
	 * @return
	 */
	public String updateListByPrimaryKeySelective(MappedStatement ms) {
		String entityName = "item";
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder(64);
		sql.append("<foreach item=\"" + entityName + "\" collection=\"list\" separator=\";\">");
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.updateSetColumns(entityClass, entityName, true, isNotEmpty()));
		sql.append(SqlHelper.wherePKColumns(entityClass, entityName, true));
		sql.append("</foreach>");
		return sql.toString();
	}
	
	/**
     * 根据Example批量更新
     *
     * @param ms
     * @return
     */
    public String updateListByExamples(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder(64);
		sql.append("<foreach item=\"record\" collection=\"records\" separator=\";\" index=\"i\">");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //安全更新，Example 必须包含条件
        if (getConfig().isSafeUpdate()) {
            sql.append(SqlHelper.exampleHasAtLeastOneCriteriaCheck("examples.get(i)"));
        }
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "examples.get(i)"));
        sql.append(SqlHelper.updateSetColumnsIgnoreVersion(entityClass, "record", false, false));
        sql.append(updateByExamplesWhereClause());
		sql.append("</foreach>");
		
        return sql.toString();
    }
    
    /**
     * 根据Example批量更新非null字段
     *
     * @param ms
     * @return
     */
    public String updateListByExamplesSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder(64);
		sql.append("<foreach item=\"record\" collection=\"records\" separator=\";\" index=\"i\">");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //安全更新，Example 必须包含条件
        if (getConfig().isSafeUpdate()) {
            sql.append(SqlHelper.exampleHasAtLeastOneCriteriaCheck("examples.get(i)"));
        }
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "examples.get(i)"));
        sql.append(SqlHelper.updateSetColumnsIgnoreVersion(entityClass, "record", true, isNotEmpty()));
        sql.append(updateByExamplesWhereClause());
		sql.append("</foreach>");
		
        return sql.toString();
    }
    
    /**
     * Example-Update中的where结构，用于多个参数时，List<Example>带@Param("examples")注解时
     *
     * @return
     */
    private static String updateByExamplesWhereClause() {
        return "<where>\n" +
                " ${@tk.mybatis.mapper.util.OGNL@andNotLogicDelete(examples.get(i))}" +
                " <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                "  <foreach collection=\"examples.get(i).oredCriteria\" item=\"criteria\">\n" +
                "    <if test=\"criteria.valid\">\n" +
                "      ${@tk.mybatis.mapper.util.OGNL@andOr(criteria)}" +
                "      <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
                "          <choose>\n" +
                "            <when test=\"criterion.noValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
                "            </when>\n" +
                "            <when test=\"criterion.singleValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value}\n" +
                "            </when>\n" +
                "            <when test=\"criterion.betweenValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                "            </when>\n" +
                "            <when test=\"criterion.listValue\">\n" +
                "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
                "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                "                #{listItem}\n" +
                "              </foreach>\n" +
                "            </when>\n" +
                "          </choose>\n" +
                "        </foreach>\n" +
                "      </trim>\n" +
                "    </if>\n" +
                "  </foreach>\n" +
                " </trim>\n" +
                "</where>";
    }

}