package com.yuan.cloud.core.common.enums;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;

/**
 * @author Mr.Y
 * Created on 2024-11-28 14:21
 * @description 返回状态枚举类
 */
@Getter
@Tag(name = "YuanStatusEnum", description = "返回状态枚举类，定义了系统内返回状态码和默认提示信息")
public enum YuanStatusEnum {
    // ======== 2xx : 请求成功 ==========
    SUCCESS(200, "操作成功"),

    // ======== 1xx : 内部接口错误 ==========
    // ======== 10xx : 认证相关 ==========
    CAPTCHA_INVALID(1001, "验证码无效或已超时"),
    CAPTCHA_CODE_NOT_EQUALS(1002, "验证码匹配失败"),
    USER_NOT_FOUND(1003, "用户不存在"),
    PASSWORD_ERROR(1004, "密码错误"),
    USER_EXIST_ERROR(1005, "用户名已存在"),
    OAUTH2_USERNAME_NOT_EXISTS(1006, "用户名不能为空"),
    OAUTH2_PASSWORD_NOT_EXISTS(1007, "密码不能为空"),
    INVALID_CLIENT(1008, "客户端信息异常"),
    UNAUTHORIZED_CLIENT(1009, "客户端不支持当前认证方式"),
    ASSESS_TOKEN_GENERATE_ERROR(1010, "assessToken生成失败"),
    FEIGN_API_ERROR(1011, "feign接口调用异常"),
    CLIENT_API_ERROR(1012, "客户端接口调用异常"),

    // ======== 11xx : 参数缺失/校验错误 ==========
    REQUEST_PARAM_VALID_ERROR(1101, "参数校验失败"),
    REQUEST_PARAM_MISSING(1102, "参数缺失"),
    QUERY_CONDITION_ERROR(1103, "查询条件拼接失败"),
    TOKEN_INVALID(1104, "token无效"),
    CAPTCHA_CODE_MISSING(1105, "验证码信息缺失"),
    CAPTCHA_TYPE_NOT_EXIST(1106, "验证码类型不存在"),
    UPDATE_ID_MISSING(1107, "更新方法ID不能为空"),

    // ======== 12xx : 请求错误 ==========
    UNAUTHORIZED(1201, "无权限访问"),
    METHOD_NOT_ALLOWED(1202, "不允许的方法"),
    TOKEN_NOT_EXIST(1203, "客户端请求token不存在"),
    FILE_UPLOAD_FAIL(1204, "文件上传失败"),
    FILE_REMOVE_FAIL(1205, "文件删除失败"),
    DATA_NOT_EXIST(1206, "未查询到相关数据"),

    // ======== 13xx : 过期错误 ==========
    TOKEN_EXPIRED(1301, "token过期"),

    // ======== 4xx : 请求错误 ==========
    FAIL(400, "操作失败"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源未找到"),
    REQUEST_TIMEOUT(408, "请求超时"),

    // ======== 5xx : 服务器错误 ==========
    INTERNAL_SERVER_ERROR(500, "服务器错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final int code;
    private final String msg;

    YuanStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
