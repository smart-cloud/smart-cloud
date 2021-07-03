package org.smartframework.cloud.starter.mybatis.common.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.smartframework.cloud.starter.core.business.util.SnowFlakeIdUtil;
import org.smartframework.cloud.starter.mybatis.common.mapper.SmartMapper;
import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;
import org.smartframework.cloud.starter.mybatis.common.mapper.enums.DelStateEnum;
import org.smartframework.cloud.starter.mybatis.util.ClassUtil;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Date;

public class BaseBiz<T extends BaseEntity> extends ServiceImpl<SmartMapper<T>, T> {

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
        Class<T> clazz = ClassUtil.getActualTypeArgumentFromSuperGenericClass(getClass(), 0);
        T entity = BeanUtils.instantiateClass(clazz);
        entity.setId(SnowFlakeIdUtil.getInstance().nextId());
        entity.setInsertTime(new Date());
        entity.setDelState(DelStateEnum.NORMAL.getDelState());
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
        Class<T> clazz = ClassUtil.getActualTypeArgumentFromSuperGenericClass(getClass(), 0);
        T entity = BeanUtils.instantiateClass(clazz);
        entity.setId(id);
        entity.setDelUser(uid);
        entity.setDelTime(new Date());
        entity.setDelState(DelStateEnum.DELETED.getDelState());
        return baseMapper.updateById(entity) == 1;
    }

}