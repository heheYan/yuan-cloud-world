package com.yuan.cloud.auth.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author YuAN
 * Created on 2025-05-08 22:34
 * @description 验证码VO对象
 */
@Getter
@Setter
public class YaCaptchaVO {
    /**
     * 验证码ID
     */
    private String captchaId;
    /**
     * 验证码图片
     */
    private String captchaImg;
    /**
     * 验证码类型
     */
    private String captchaType;
    /**
     * 验证码值
     */
    private String captchaCode;
    /**
     * 验证码过期时间
     */
    private int captchaExpireAt;

}
