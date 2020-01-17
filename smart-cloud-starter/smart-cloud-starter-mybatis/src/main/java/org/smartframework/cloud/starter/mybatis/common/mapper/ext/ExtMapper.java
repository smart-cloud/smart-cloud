package org.smartframework.cloud.starter.mybatis.common.mapper.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.vo.BaseEntityRespVO;
import org.smartframework.cloud.common.pojo.vo.BasePageRespVO;
import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper.InsertListSelectiveMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper.LogicDeleteMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper.UpdateListByExamplesMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper.UpdateListByExamplesSelectiveMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper.UpdateListByPrimaryKeyMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.ext.mapper.UpdateListByPrimaryKeySelectiveMapper;
import org.smartframework.cloud.starter.mybatis.util.ClassUtil;
import org.springframework.beans.BeanUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.additional.update.force.UpdateByPrimaryKeySelectiveForceMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.entity.Example;

/**
 * 通用mapper超类
 *
 * <p>
 * <b>NOTE</b>：
 * <ul>
 * <li>updateByExample
 * <li>UpdateByExampleSelectiveMapper
 * <li>DeleteByExampleMapper
 * </ul>
 * 当调用以上方法时，如果条件传入的值为null，可能会产生误操作；对此可以用
 * <code>Example example = new Example(ProductInfoEntity.class, true, true);</code>
 * 类似这种方式创建<code>Example</code>
 *
 * @param <T> entity
 * @param <R> entity对应的resp对象
 * @param <PK> 表主键类型
 * @author liyulin
 * @date 2019-03-31
 */
@RegisterMapper
public interface ExtMapper<T extends BaseEntity, R extends BaseEntityRespVO, PK> extends Mapper<T>,
		IdListMapper<T, PK>, InsertListMapper<T>, InsertListSelectiveMapper<T>, UpdateListByPrimaryKeyMapper<T>,
		UpdateListByPrimaryKeySelectiveMapper<T>, UpdateByPrimaryKeySelectiveForceMapper<T>,
		UpdateListByExamplesMapper<T>, UpdateListByExamplesSelectiveMapper<T>, LogicDeleteMapper<T>, Marker {

	/**
	 * 根据主键id查询resp对象
	 * 
	 * @param id
	 * @return
	 */
	default R selectRespById(Long id) {
		T entitydata = selectByPrimaryKey(id);
		if (Objects.isNull(entitydata)) {
			return null;
		}

		Class<R> clazz = ClassUtil.getActualTypeArgumentFromSuperGenericInterface(getClass(), 1);
		R r = BeanUtils.instantiateClass(clazz);
		BeanUtils.copyProperties(entitydata, r);
		return r;
	}
	
	/**
	 * 根据example条件分页查询，返回entity对象
	 * 
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	default BasePageRespVO<T> pageEntityByExample(Example example, int pageNum, int pageSize) {
		Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
		List<T> datas = selectByExample(example);

		return new BasePageRespVO<>(datas, pageNum, pageSize, page.getTotal());
	}

	/**
	 * 根据example条件分页查询，返回resp对象
	 * 
	 * @param example
	 * @param clazz
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	default BasePageRespVO<R> pageRespByExample(Example example, int pageNum, int pageSize) {
		Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
		List<T> entitydatas = selectByExample(example);
		if (CollectionUtils.isEmpty(entitydatas)) {
			return new BasePageRespVO<>(null, pageNum, pageSize, page.getTotal());
		}

		Class<R> clazz = ClassUtil.getActualTypeArgumentFromSuperGenericInterface(getClass(), 1);
		List<R> respDatas = new ArrayList<>(entitydatas.size());
		for (T item : entitydatas) {
			R r = BeanUtils.instantiateClass(clazz);
			BeanUtils.copyProperties(item, r);
			respDatas.add(r);
		}

		return new BasePageRespVO<>(respDatas, pageNum, pageSize, page.getTotal());
	}

	/**
	 * 根据entity条件分页查询，返回entity对象
	 * 
	 * @param entity
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	default BasePageRespVO<T> pageEntityByEntity(T entity, int pageNum, int pageSize) {
		Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
		List<T> datas = select(entity);

		return new BasePageRespVO<>(datas, pageNum, pageSize, page.getTotal());
	}

	/**
	 * 根据example条件分页查询，返回resp对象
	 * 
	 * @param example
	 * @param clazz
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	default BasePageRespVO<R> pageRespByEntity(T entity, int pageNum, int pageSize) {
		Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
		List<T> entitydatas = select(entity);
		if (CollectionUtils.isEmpty(entitydatas)) {
			return new BasePageRespVO<>(null, pageNum, pageSize, page.getTotal());
		}

		Class<R> clazz = ClassUtil.getActualTypeArgumentFromSuperGenericInterface(getClass(), 1);
		List<R> respDatas = new ArrayList<>(entitydatas.size());
		for (T item : entitydatas) {
			R r = BeanUtils.instantiateClass(clazz);
			BeanUtils.copyProperties(item, r);
			respDatas.add(r);
		}

		return new BasePageRespVO<>(respDatas, pageNum, pageSize, page.getTotal());
	}

}