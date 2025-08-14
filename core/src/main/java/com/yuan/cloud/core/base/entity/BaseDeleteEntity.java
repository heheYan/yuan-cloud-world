package com.yuan.cloud.core.base.entity;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;

/**
 * Description 具备逻辑删除字段的父类
 *
 * @author Mr.Y
 * Created on 2025-08-06 16:15
 */
@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
@SoftDelete(columnName = "is_deleted")
@Tag(name = "BaseDeleteEntity", description = "逻辑删除父类，继承基础父类")
public abstract class BaseDeleteEntity extends AbstractBaseEntity {
}
