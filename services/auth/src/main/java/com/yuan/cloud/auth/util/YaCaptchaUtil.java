package com.yuan.cloud.auth.util;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.yuan.cloud.auth.vo.YaCaptchaVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author YuAN
 * Created on 2025-05-08 22:36
 * description 认证相关工具类
 */
@Slf4j
public class YaCaptchaUtil {
    private YaCaptchaUtil() {
        throw new UnsupportedOperationException("Utility classes cannot be instantiated.");
    }

    /**
     * 构建验证码VO
     *
     * @return 验证码VO
     */
    public static YaCaptchaVO buildCaptchaVO() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100, 5, 3);

        return buildCaptchaVO(IdUtil.fastSimpleUUID(),
                lineCaptcha.getImageBase64Data(),
                "Line",
                lineCaptcha.getCode(),
                120);
    }

    /**
     * 构建验证码VO
     *
     * @param captchaId       验证码ID
     * @param captchaImg      验证码图片
     * @param captchaType     验证码类型
     * @param captchaCode     验证码
     * @param captchaExpireAt 验证码过期时间
     * @return 验证码VO
     */
    public static YaCaptchaVO buildCaptchaVO(String captchaId, String captchaImg, String captchaType, String captchaCode, int captchaExpireAt) {
        YaCaptchaVO captchaVO = new YaCaptchaVO();
        captchaVO.setCaptchaId(captchaId);
        captchaVO.setCaptchaImg(captchaImg);
        captchaVO.setCaptchaType(captchaType);
        captchaVO.setCaptchaCode(captchaCode);
        captchaVO.setCaptchaExpireAt(DateUtil.offsetSecond(new Date(), captchaExpireAt).getTime());
        return captchaVO;
    }
}
