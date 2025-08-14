package com.yuan.cloud.core.base.entity;

import com.yuan.cloud.core.base.generator.SnowIdGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Description 所有实体类继承的抽象父类
 *
 * @author Mr.Y
 * Created on 2025-08-06 16:11
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class) // 添加审计监听器
@Tag(name = "AbstractBaseEntity", description = "抽象父类")
public abstract class AbstractBaseEntity {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(generator = "snowId")
    @GenericGenerator(name = "snowId", type = SnowIdGenerator.class)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
