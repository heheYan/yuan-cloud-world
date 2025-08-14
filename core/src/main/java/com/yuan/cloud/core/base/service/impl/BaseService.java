package com.yuan.cloud.core.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import com.yuan.cloud.core.base.query.AbstractBaseQuery;
import com.yuan.cloud.core.base.query.YaPageQuery;
import com.yuan.cloud.core.base.query.util.QueryBuilderUtil;
import com.yuan.cloud.core.base.repository.BaseRepository;
import com.yuan.cloud.core.base.service.IBaseService;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Description 基础service父类
 *
 * @author Mr.Y
 * Created on 2025-08-06 17:20
 */
@Slf4j
public class BaseService<T extends AbstractBaseEntity, Q extends AbstractBaseQuery> implements IBaseService<T, Q> {

    private final BaseRepository<T> baseRepository;

    // 构造函数注入
    public BaseService(BaseRepository<T> baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体对象
     */
    @Override
    public T findById(Long id) {
        return baseRepository.findById(id).orElse(null);
    }

    /**
     * 保存
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    @Override
    public T save(T entity) {
        return baseRepository.save(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    @Override
    public T update(T entity) {
        // 判断ID是否存在
        if (entity.getId() == null) {
            throw new YuanApiException(YuanStatusEnum.UPDATE_ID_MISSING);
        }
        // 判断数据是否存在
        T t = baseRepository.findById(entity.getId()).orElse(null);
        if (t == null) {
            throw new YuanApiException(YuanStatusEnum.NOT_FOUND);
        }
        // 拷贝属性,忽略空值
        BeanUtil.copyProperties(entity, t, CopyOptions.create().ignoreNullValue().ignoreCase());
        return baseRepository.save(t);
    }

    /**
     * 根据ID主键删除
     *
     * @param id 主键
     */
    @Override
    public boolean deleteById(Long id) {
        baseRepository.deleteById(id);
        return !baseRepository.existsById(id);
    }

    /**
     * 根据条件查询列表
     *
     * @param condition 查询条件
     * @return
     */
    @Override
    public List<T> listAll(Q condition) {
        return baseRepository.findAll();
    }

    /**
     * 根据ID主键查询
     *
     * @param ids 主键
     * @return 所有实体对象
     */
    @Override
    public List<T> listByIds(List<Long> ids) {
        return baseRepository.findAllById(ids);
    }

    /**
     * 统计数量
     *
     * @return 数量
     */
    @Override
    public long count() {
        return baseRepository.count();
    }

    /**
     * 根据ID主键批量删除
     *
     * @param ids 主键
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        baseRepository.deleteAllById(ids);
        return ids.size();
    }

    /**
     * 判断是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    @Override
    public boolean existsById(Long id) {
        return baseRepository.existsById(id);
    }

    /**
     * 分页查询
     *
     * @param page      页码
     * @param size      页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    @Override
    public Page<T> page(int page, int size, Q condition) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Specification<T> specification = QueryBuilderUtil.builder(condition);
        return baseRepository.findAll(specification, pageable);
    }

    /**
     * 分页查询
     *
     * @param condition 封装查询条件
     * @return 分页结果
     */
    @Override
    public Page<T> page(YaPageQuery<Q> condition) {
        // 获取查询条件
        Q query = condition.getQuery();
        // 获取分页信息
        Pageable pageable = PageRequest.of(condition.getCurrentPage(), condition.getPageSize(),
                Sort.by(Sort.Direction.fromString(condition.getIsAsc() ? "ASC" : "DESC"), condition.getSortBy()));
        // 获取查询条件
        Specification<T> specification = QueryBuilderUtil.builder(query);
        return baseRepository.findAll(specification, pageable);
    }
}
