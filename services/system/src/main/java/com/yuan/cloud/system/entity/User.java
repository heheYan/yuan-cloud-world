package com.yuan.cloud.system.entity;

import com.yuan.cloud.core.base.entity.BaseDeleteEntity;
import com.yuan.cloud.core.module.system.enums.UserStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "s_user")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false)),
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", updatable = false)),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
@ToString
public class User extends BaseDeleteEntity {
    @Size(max = 255)
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @Column(name = "pwd")
    private String pwd;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "status", length = 4)
    private String status = UserStatusEnum.NORMAL.getCode();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinTable(name = "s_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();
}