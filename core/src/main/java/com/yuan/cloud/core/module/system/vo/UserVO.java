package com.yuan.cloud.core.module.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yuan.cloud.core.base.vo.AbstractBaseVO;
import com.yuan.cloud.core.common.serializer.EmailSerializer;
import com.yuan.cloud.core.common.serializer.MobileSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.yuan.cloud.system.entity.User}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends AbstractBaseVO implements Serializable {
    String username;
    String nickName;
    String avatar;
    /**
     * 邮箱脱敏
     */
    @JsonSerialize(using = EmailSerializer.class)
    private String email;

    /**
     * 手机号脱敏
     */
    @JsonSerialize(using = MobileSerializer.class)
    private String mobile;

    String status;

    List<RoleVO> roles;
}
