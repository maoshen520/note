package com.yun.note.config;

import com.yun.note.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


//拦截器配置  ---此处注册拦截器 WebMvcConfigurer
@Configuration   //@Configuration注解的作用：声明一个类为配置类，用于取代bean.xml配置文件注册bean对象
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private UserTokenInterceptor userTokenInterceptor;  //userToken  拦截器

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //  拦截器注册
        registry.addInterceptor(userTokenInterceptor);
    }
}
