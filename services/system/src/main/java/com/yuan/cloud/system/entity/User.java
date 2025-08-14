package com.yuan.cloud.system.entity;

import com.yuan.cloud.core.base.entity.BaseDeleteEntity;
import com.yuan.cloud.core.module.system.enums.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Schema(description = "用户信息实体")
public class User extends BaseDeleteEntity {
    @SchemaProperty(name = "用户名，登录账号")
    @Size(max = 255)
    @Column(name = "username")
    private String username;

    @SchemaProperty(name = "密码")
    @Size(max = 255)
    @Column(name = "pwd")
    private String pwd;

    @SchemaProperty(name = "昵称")
    @Column(name = "nick_name")
    private String nickName;

    @SchemaProperty(name = "头像链接")
    @Column(name = "avatar")
    private String avatar;

    @SchemaProperty(name = "邮箱")
    @Column(name = "email")
    private String email;

    @SchemaProperty(name = "手机号")
    @Column(name = "mobile")
    private String mobile;

    @SchemaProperty(name = "微信ID")
    @Column(name = "open_id")
    private String openId;

    @SchemaProperty(name = "用户状态，默认正常")
    @Column(name = "status", length = 4)
    private String status = UserStatusEnum.NORMAL.getCode();

    @SchemaProperty(name = "用户角色")
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinTable(name = "s_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private List<Role> roles;
}