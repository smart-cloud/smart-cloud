package org.smartframework.cloud.starter.mybatis.common.mapper.ext.provider;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 逻辑删除
 *
 * @author liyulin
 * @date 2019-03-24
 */
public class LogicDeleteProvider extends MapperTemplate {

	public LogicDeleteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 逻辑删除
	 *
	 * @param ms
	 * @return
	 */
	public String logicDeleteByPrimaryKey(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append("SET f_sys_del_state = 2 ");
		sql.append("<if test=\"delTime != null\">,f_sys_del_time = #{delTime}</if>");
		sql.append("<if test=\"delUser != null\">,f_sys_del_user = #{delUser}</if>");
		sql.append("WHERE f_id =#{id} AND f_sys_del_state=1");
		return sql.toString();
	}

	/**
	 * 批量逻辑删除
	 *
	 * @param ms
	 * @return
	 */
	public String logicDeleteByPrimaryKeys(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append("SET f_sys_del_state = 2 ");
		sql.append("<if test=\"delTime != null\">,f_sys_del_time = #{delTime}</if>");
		sql.append("<if test=\"delUser != null\">,f_sys_del_user = #{delUser}</if>");
		sql.append("WHERE f_id ");
		sql.append("<foreach item=\"id\" collection=\"ids\" open=\"IN(\" close=\")\" separator=\",\">");
		sql.append("#{id}");
		sql.append("</foreach>");
		sql.append("AND f_sys_del_state=1");
		return sql.toString();
	}
	
	/**
	 * 逻辑删除全表
	 *
	 * @param ms
	 * @return
	 */
	public String logicDeleteAll(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append("SET f_sys_del_state = 2 ");
		sql.append("<if test=\"delTime != null\">,f_sys_del_time = #{delTime}</if>");
		sql.append("<if test=\"delUser != null\">,f_sys_del_user = #{delUser}</if>");
		sql.append("WHERE f_sys_del_state=1");
		return sql.toString();
	}

}