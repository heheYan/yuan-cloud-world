package com.yuan.cloud.core.common.response;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpStatus;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description 返回对象
 *
 * @author Mr.Y
 * Created on 2025-08-08 09:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Tag(name = "YuanR", description = "通用接口返回对象")
public class YuanR<T> implements Serializable {

    @Schema(name = "code", description = "状态码")
    private int code;
    @Schema(name = "message", description = "响应信息")
    private String message;
    @Schema(name = "data", description = "返回的数据泛型")
    private T data;
    @Schema(name = "timestamp", description = "响应时间")
    private long timestamp;

    @Operation(description = "响应成功,默认返回null")
    public static <T> YuanR<T> ok() {
        return ok(null);
    }

    @Operation(description = "响应成功，返回传入的数据",
            parameters = {
                    @Parameter(name = "data", description = "返回的数据")
            })
    public static <T> YuanR<T> ok(T data) {
        return ok("接口执行成功", data);
    }

    @Operation(description = "响应成功，返回传入的信息和数据",
            parameters = {
                    @Parameter(name = "msg", description = "响应信息"),
                    @Parameter(name = "data", description = "返回的数据")
            })
    public static <T> YuanR<T> ok(String msg, T data) {
        return ok(HttpStatus.HTTP_OK, msg, data);
    }

    @Operation(description = "响应成功，完整返回",
            parameters = {
                    @Parameter(name = "code", description = "状态码"),
                    @Parameter(name = "msg", description = "响应信息"),
                    @Parameter(name = "data", description = "返回的数据")
            })
    public static <T> YuanR<T> ok(int code, String msg, T data) {
        return new YuanR<T>(code, msg, data, DateUtil.current());
    }

    @Operation(description = "响应失败，默认返回失败信息",
            parameters = {
                    @Parameter(name = "msg", description = "响应信息")
            })
    public static <T> YuanR<T> fail(String msg) {
        return fail(msg, null);
    }

    @Operation(description = "响应失败，根据自定义的异常枚举返回错误信息",
            parameters = {
                    @Parameter(name = "yuanStatusEnum", description = "自定义的异常枚举类")
            })
    public static <T> YuanR<T> fail(YuanStatusEnum yuanStatusEnum) {
        return fail(yuanStatusEnum.getCode(), yuanStatusEnum.getMsg());
    }

    @Operation(description = "响应失败，根据自定义的异常枚举返回错误信息",
            parameters = {
                    @Parameter(name = "yuanStatusEnum", description = "自定义的异常枚举类"),
                    @Parameter(name = "data", description = "返回的数据")
            })
    public static <T> YuanR<T> fail(YuanStatusEnum yuanStatusEnum, T data) {
        return fail(yuanStatusEnum.getCode(), yuanStatusEnum.getMsg(), data);
    }

    @Operation(description = "响应失败，返回传入的状态码和错误信息",
            parameters = {
                    @Parameter(name = "code", description = "状态码"),
                    @Parameter(name = "msg", description = "响应信息")
            })
    public static <T> YuanR<T> fail(int code, String msg) {
        return fail(code, msg, null);
    }

    @Operation(description = "响应失败，默认错误状态码",
            parameters = {
                    @Parameter(name = "msg", description = "响应信息"),
                    @Parameter(name = "data", description = "返回的数据")
            })
    public static <T> YuanR<T> fail(String msg, T data) {
        return fail(HttpStatus.HTTP_BAD_REQUEST, msg, data);
    }

    @Operation(description = "响应失败，完整返回",
            parameters = {
                    @Parameter(name = "code", description = "状态码"),
                    @Parameter(name = "msg", description = "响应信息"),
                    @Parameter(name = "data", description = "返回的数据")
            })
    public static <T> YuanR<T> fail(int code, String msg, T data) {
        return new YuanR<T>(code, msg, data, DateUtil.current());
    }

    @Schema(name = "isSuccess", description = "是否相应成功")
    public boolean isSuccess() {
        return code == HttpStatus.HTTP_OK;
    }

    public static YuanR<?> success() {
        return ok();
    }
}
