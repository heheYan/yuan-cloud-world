package com.yuan.cloud.core.base.service;

import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import com.yuan.cloud.core.base.query.AbstractBaseQuery;
import com.yuan.cloud.core.base.query.YaPageQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Description 基础service接口
 *
 * @author Mr.Y
 * Created on 2025-08-06 17:34
 */
public interface IBaseService<T extends AbstractBaseEntity, Q extends AbstractBaseQuery> {
    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体对象
     */
    T findById(Long id);

    /**
     * 保存
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    T save(T entity);

    /**
     * 更新
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    T update(T entity);

    /**
     * 根据ID主键删除
     *
     * @param id 主键
     */
    boolean deleteById(Long id);

    /**
     * 根据条件查询列表
     *
     * @param condition 查询条件
     * @return 所有实体对象
     */
    List<T> listAll(Q condition);

    /**
     * 根据ID主键查询
     *
     * @param ids 主键
     * @return 所有实体对象
     */
    List<T> listByIds(List<Long> ids);

    /**
     * 统计数量
     *
     * @return 数量
     */
    long count();

    /**
     * 根据ID主键批量删除
     *
     * @param ids 主键
     */
    int deleteByIds(List<Long> ids);

    /**
     * 判断是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    boolean existsById(Long id);

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  页大小
     * @param query 查询条件
     * @return 分页结果
     */
    Page<T> page(int page, int size, Q query);

    /**
     * 分页查询
     *
     * @param condition 分页条件
     * @return 分页结果
     */
    Page<T> page(YaPageQuery<Q> condition);
}
