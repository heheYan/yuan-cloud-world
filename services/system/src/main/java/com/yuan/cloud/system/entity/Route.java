package com.yuan.cloud.system.entity;

import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Entity
@Table(name = "s_route")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false)),
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
@DynamicUpdate
public class Route extends AbstractBaseEntity {
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Size(max = 4)
    @Column(name = "type", length = 4)
    private String type;

    @Size(max = 255)
    @Column(name = "path")
    private String path;

    @Size(max = 255)
    @Column(name = "icon")
    private String icon;

    @Column(name = "p_id")
    private Long pId;

    @Size(max = 255)
    @Column(name = "p_code")
    private String pCode;

    @Column(name = "sorted")
    private Integer sorted;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

}