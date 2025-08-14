package com.yuan.cloud.core.common.exception;

import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mr.Y
 * Created on 2024-11-28 20:28
 * description 项目通用接口异常
 */
@Getter
@Setter
@Tag(name = "YuanApiException", description = "项目通用接口异常类")
public class YuanApiException extends RuntimeException {
    private YuanStatusEnum errEnum;

    public YuanApiException(String message) {
        super(message);
        errEnum = YuanStatusEnum.FAIL;
    }

    public YuanApiException(YuanStatusEnum errEnum) {
        super(errEnum.getMsg());
        this.errEnum = errEnum;
    }
}
