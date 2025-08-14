package com.yuan.cloud.system.entity;

import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "s_role")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false)),
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
@DynamicUpdate
public class Role extends AbstractBaseEntity {
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "is_enabled")
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Route.class)
    @JoinTable(name = "s_role_route",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id"))
    private List<Route> routes;
}