package com.yun.note.annotation;

import java.lang.annotation.*;

//  注解： 需要验证请求头中的 userToken
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserToken {
}
