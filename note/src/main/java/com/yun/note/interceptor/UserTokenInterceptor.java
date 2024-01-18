package com.yun.note.interceptor;

import cn.hutool.json.JSONUtil;
import com.yun.note.annotation.UserToken;
import com.yun.note.exception.ValidateParamException;
import com.yun.note.service.IUserTokenService;
import com.yun.note.util.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


//  userToken 拦截器---
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    //　1、实现HandlerInterceptor接口，该接口有三个方法preHandle 、postHandle 、afterCompletion
    // 　　（1）preHandle在controller执行之前调用
    // 　　（2）postHandle在controller执行之后，页面渲染之前调用
    // 　　（3）afterCompletion在页面渲染之后调用，一般用于资源清理操作
    //　2、继承WebMvcConfigurationSupport或者实现WebMvcConfigurer，重写addInterceptors方法。把拦截器加进去。

    @Autowired
    private IUserTokenService userTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果映射的不是方法，则直接放行
        if(!(handler instanceof HandlerMethod)) return true;

        //映射的方法对象
        Method method = ((HandlerMethod) handler).getMethod();

        //如果该方法上没有 @userToken 注解，则直接放行
        if(!method.isAnnotationPresent(UserToken.class)) return true;

        //验证请求头中的 userToken 参数
        try {
            int id = userTokenService.validDator(request.getHeader("userToken"));
            //在请求作用域里存放用户的编号
            request.setAttribute("userTokenID", id);
            // 放行
            return true;
        } catch (ValidateParamException e) {
            e.printStackTrace();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(JSONUtil.toJsonStr(ResponseData.error(e.getMessage(), e.getCode())));

            // 拦截
            return false;
        }



    }
}
