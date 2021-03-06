package org.smartframework.cloud.starter.mybatis.common.biz;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.BaseEntityResponse;
import org.smartframework.cloud.common.pojo.BasePageRequest;
import org.smartframework.cloud.common.pojo.BasePageResponse;
import org.smartframework.cloud.starter.core.business.util.SnowFlakeIdUtil;
import org.smartframework.cloud.starter.mybatis.common.mapper.SmartMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.constants.DelState;
import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BaseBiz<M extends SmartMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    /**
     * 生成id
     *
     * @return
     */
    protected long generateId() {
        return SnowFlakeIdUtil.getInstance().nextId();
    }

    /**
     * 创建待插入数据的实体对象
     *
     * @return
     */
    public T create() {
        T entity = BeanUtils.instantiateClass(entityClass);
        entity.setId(SnowFlakeIdUtil.getInstance().nextId());
        entity.setInsertTime(new Date());
        entity.setDelState(DelState.NORMAL);
        return entity;
    }


    /**
     * in-line式批量插入
     *
     * @param entityList
     * @return
     */
    public Integer insertBatchSomeColumn(Collection<T> entityList) {
        return baseMapper.insertBatchSomeColumn(entityList);
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @param uid
     * @return
     */
    public Boolean logicDelete(Long id, Long uid) {
        T entity = BeanUtils.instantiateClass(entityClass);
        entity.setId(id);
        entity.setDelUser(uid);
        entity.setDelTime(new Date());
        entity.setDelState(DelState.DELETED);
        return baseMapper.updateById(entity) == 1;
    }

    /**
     * 分页查询表字段信息
     *
     * @param q
     * @param wrapper
     * @param pageItemClass
     * @param <R>
     * @param <Q>
     * @return
     */
    public <R extends BaseEntityResponse, Q extends BasePageRequest> BasePageResponse<R> page(Q q, Wrapper<T> wrapper, Class<R> pageItemClass) {
        IPage<T> page = super.page(new Page<>(q.getPageNum(), q.getPageSize(), true), wrapper);
        List<T> entityDatas = page.getRecords();
        if (CollectionUtils.isEmpty(entityDatas)) {
            return new BasePageResponse<>(null, q.getPageNum(), q.getPageSize(), 0);
        }

        List<R> pageDatas = entityDatas.stream()
                .map(entity -> {
                    R r = BeanUtils.instantiateClass(pageItemClass);
                    BeanUtils.copyProperties(entity, r);
                    return r;
                }).collect(Collectors.toList());

        return new BasePageResponse<>(pageDatas, q.getPageNum(), q.getPageSize(), page.getTotal());
    }

}