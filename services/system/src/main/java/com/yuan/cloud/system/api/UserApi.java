package com.yuan.cloud.system.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.yuan.cloud.core.base.api.AbstractBaseApi;
import com.yuan.cloud.core.common.annotation.YaApi;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import com.yuan.cloud.core.module.system.dto.UserDTO;
import com.yuan.cloud.core.module.system.enums.UserStatusEnum;
import com.yuan.cloud.core.module.system.vo.UserVO;
import com.yuan.cloud.system.entity.Role;
import com.yuan.cloud.system.entity.User;
import com.yuan.cloud.system.query.UserQuery;
import com.yuan.cloud.system.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Description 用户接口
 *
 * @author Mr.Y
 * Created on 2025-08-08 10:48
 */
@Slf4j
@YaApi
@RequestMapping("/user")
@Tag(name = "用户管理模块", description = "用户相关的增删改查接口")
public class UserApi extends AbstractBaseApi<User, UserQuery, UserDTO, UserVO, IUserService> {

    private final IUserService userService;

    public UserApi(IUserService baseService) {
        super(baseService);
        userService = baseService;
    }

    @Operation(summary = "根据用户名查询用户信息(客户端内部请求)", description = "根据用户名查询用户信息",
            parameters = {@Parameter(name = "username", description = "用户名", required = true, schema = @Schema(implementation = String.class))},
            responses = {@ApiResponse(description = "用户信息", content = @Content(schema = @Schema(description = "系统用户信息")))})
    @GetMapping("findByUsername/{username}")
    public UserDTO findByUsername(@NotEmpty @PathVariable("username") String username) {
        return BeanUtil.copyProperties(userService.findByUsernameIgnoreCase(username), UserDTO.class);
    }

    @Operation(summary = "更新用户信息", description = "更新用户信息",
            parameters = @Parameter(name = "dto", description = "用户信息", schema = @Schema(implementation = UserDTO.class)),
            responses = @ApiResponse(description = "用户信息", content = @Content(schema = @Schema(implementation = UserVO.class))))
    @Override
    public UserVO update(@Validated @RequestBody UserDTO dto) {
        // 更新方法不涉及角色、密码、创建时间，设置null，在service层不会被复制
        dto.setRoles(null);
        dto.setPwd(null);
        dto.setCreatedAt(null);
        return super.update(dto);
    }

    @Operation(summary = "根据用户ID更新用户角色", description = "根据用户ID更新用户角色，其中用户ID必填",
            parameters = {@Parameter(name = "dto", schema = @Schema(implementation = UserDTO.class), description = "用户信息", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "更新成功", content = @Content(schema = @Schema(implementation = UserVO.class)))})
    @PostMapping("/updateRoles")
    public UserVO updateRoles(@RequestBody UserDTO dto) {
        // 用户ID必填
        if (dto.getId() == null) {
            throw new YuanApiException(YuanStatusEnum.UPDATE_ID_MISSING);
        }
        User user = new User();
        user.setId(dto.getId());
        user.setRoles(BeanUtil.copyToList(dto.getRoles(), Role.class));
        return BeanUtil.copyProperties(userService.update(user), UserVO.class);
    }

    @Operation(summary = "根据用户ID更新用户状态", description = "根据用户ID更新用户状态，其中用户ID必填",
            parameters = {@Parameter(name = "dto", schema = @Schema(implementation = UserDTO.class), description = "用户信息", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "更新成功", content = @Content(schema = @Schema(implementation = UserVO.class)))})
    @PostMapping("/updateStatus")
    public UserVO updateStatus(@RequestBody UserDTO dto) {
        // 用户ID必填
        if (dto.getId() == null) {
            throw new YuanApiException(YuanStatusEnum.UPDATE_ID_MISSING);
        }
        if (StrUtil.isBlankIfStr(dto.getStatus())) {
            throw new YuanApiException(YuanStatusEnum.REQUEST_PARAM_MISSING);
        }
        if (!UserStatusEnum.isValidCode(dto.getStatus())) {
            throw new YuanApiException(YuanStatusEnum.REQUEST_PARAM_VALID_ERROR);
        }
        // 校验通过，更新用户状态
        User user = new User();
        user.setId(dto.getId());
        user.setStatus(dto.getStatus());
        return BeanUtil.copyProperties(userService.update(user), UserVO.class);
    }

    @Operation(summary = "获取当前登录用户信息",
            responses = {@ApiResponse(responseCode = "200", description = "查询成功", content = @Content(schema = @Schema(implementation = UserVO.class)))})
    @GetMapping("/currentUser")
    public UserVO getCurrentUser(HttpServletRequest request) {
        String userId = request.getHeader(YaOauthConst.AUTH_HEADER_USER);
        if (StrUtil.isBlankIfStr(userId)) {
            throw new YuanApiException(YuanStatusEnum.FAIL);
        }
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            throw new YuanApiException(YuanStatusEnum.USER_NOT_FOUND);
        }
        return BeanUtil.copyProperties(user, getVoClass());
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserVO> getVoClass() {
        return UserVO.class;
    }
}
