package com.yun.note.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.SecureUtil;
import com.yun.note.exception.ServiceException;
import com.yun.note.service.IUserservice;
import com.yun.note.util.code.EventCode;
import com.yun.note.util.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 关于用户的控制层
 */


@RestController
@RequestMapping("/user")  //请求地址:http://127.0.0.1:18081/zhike-notes/user
public class UserController {

    //先去util的ResponseData设置响应数据

    //此处声明用户的业务  --下方调用邮箱密码登录业务需要
    @Autowired
    private IUserservice userservice;  //用户的业务

    @Autowired
    private StringRedisTemplate redisTemplate; // redis对象



    /**
     *登录（邮箱和密码）
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/user/login/email/password
     * @param email  邮箱号
     * @param password  密码
     * @return  响应数据{user, userToken}
     */
    @PostMapping("/login/email/password")
    public <Map> ResponseData loginByEmailAndPassword(String email, String password) {

        //验证邮箱参数是否为空
        if(Validator.isEmpty(email)){
            return new ResponseData(false,"邮箱参数有误", EventCode.ACCOUNT_EMAIL_WROME);
        }

        //验证密码参数是否为空
        if(Validator.isEmpty(password)){
            return new ResponseData(false,"密码参数有误", EventCode.ACCOUNT_EMAIL_PASSWORD);
        }

        //密码加密
        password = SecureUtil.md5(password);

        //调用邮箱密码登录业务  --在上方声明
        //userservice.loginByEmailAndPassword(email,password);
        try {
            Map loginMap = (Map) userservice.loginByEmailAndPassword(email, password);
            return new ResponseData(true,"登录成功", EventCode.LOGIN_EMAIL_PASSWORD_SUCCESS,loginMap);
        } catch (ServiceException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(),e.getCode());
        }

    }



    /**
     *  注册
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/user/register/email
     * @param email  邮箱号
     * @param vc  验证码
     * @param vcKey 验证码查询的关键词
     * @return  响应数据{user, userToken}
     */

    @PostMapping("/register/email")
    public  ResponseData registerAccountByEmail(String email, String vc, String vcKey) {

        //验证邮箱是否为空
        if(Validator.isEmpty(email)){
            return new ResponseData(false,"邮箱参数有误", EventCode.ACCOUNT_EMAIL_WROME);
        }

        //验证验证码是否为空
        if(Validator.isEmpty(vc)){
            return new ResponseData(false,"验证码参数有误", EventCode.PARAM_VC_WRONG);
        }

        //验证码查询的关键词
        if(Validator.isEmpty(vcKey)){
            return new ResponseData(false,"验证码查询关键词参数有误", EventCode.PARAM_VC_KEY_WRONG);
        }

        //判断注册的邮箱号是否为发送验证码的邮箱
        String vc_email = vcKey.split(":")[1];
        if(!CharSequenceUtil.equals(email, vc_email)){
            return new ResponseData(false, "注册邮箱已发生改变，请重新获取验证码", EventCode.PARAM_VC_KEY_EMAIL_WRONG);
        }

        //从redis中获取真实的验证码
        String vcTokenValue = redisTemplate.opsForValue().get(vcKey);
        System.out.println(vcTokenValue);




        //判断验证码是否已经失效
        if(Validator.isEmpty(vcTokenValue)){
            return new ResponseData(false,"验证码已失效，请重新获取验证码", EventCode.VC_INVALID);
        }
        //判断验证码是否准确
        if(!CharSequenceUtil.equals(vcTokenValue, vc)){
            return new ResponseData(false, "验证码错误", EventCode.VC_MATCH_ERROR);
        }

        try {
            //调用邮箱注册的业务  --在上方声明
            userservice.registerAccountByEmail(email);
            return new ResponseData(true,"注册成功", EventCode.ACCOUNT_EMAIL_REGISTER_SUCCESS);
        } catch (ServiceException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(),e.getCode());
        }
    }



    /**
     *退出登录（邮箱和密码）
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/user/login/out
     * @param userToken  邮箱号

     * @return  响应数据{user, userToken}
     */
    @GetMapping("/login/out")
    public ResponseData signOutlogin(@RequestHeader String userToken) {

        //验证userToken参数是否为空
        if(Validator.isEmpty(userToken)){
            return new ResponseData(false,"登录状态有误", EventCode.PARAM_USER_TOKEN_WRONG);
        }

        try {
            redisTemplate.delete(userToken);
            return new ResponseData(true,"退出登录成功", EventCode.LOGIN_OUT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(false,"退出登录失败", EventCode.LOGIN_OUT_EXCEPTION);
        }
    }






}
