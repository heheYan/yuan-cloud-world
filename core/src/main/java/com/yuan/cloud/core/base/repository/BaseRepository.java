package com.yuan.cloud.core.base.repository;

import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Description 基础dao父类
 *
 * @author Mr.Y
 * Created on 2025-08-06 17:13
 */
@NoRepositoryBean
public interface BaseRepository<T extends AbstractBaseEntity>
        extends PagingAndSortingRepository<T, Long>, JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
