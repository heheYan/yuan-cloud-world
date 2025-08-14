package com.yuan.cloud.core.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author Mr.Y
 * Created on 2025-05-20 22:50
 * @description controller注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface YaApi {
    /**
     * Alias for {@link Controller#value}.
     */
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
