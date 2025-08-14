package com.yuan.cloud.core.common.constant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Description 通用常量类
 *
 * @author Mr.Y
 * Created on 2025-08-11 16:34
 */
@Schema(name = "YaCommonConst", description = "通用常量类")
public class YaCommonConst {
    /**
     * 请求头中的TraceId
     */
    public static final String REQ_HEADER_TRACE_ID = "Ya-Trace-Id";
    /**
     * 认证头参数
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    /**
     * 认证类型前缀
     */
    public static final String BEARER_TOKEN_TYPE = "Bearer ";
}
