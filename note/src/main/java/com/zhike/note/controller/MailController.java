package com.zhike.note.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.service.IMailservice;
import com.zhike.note.service.IUserservice;
import com.zhike.note.util.code.EventCode;
import com.zhike.note.util.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于邮箱的控制层
 */


@RestController
@RequestMapping("/mail")  //请求地址:http://127.0.0.1:18081/zhike-notes/mail
public class MailController {

    //先去util的ResponseData设置响应数据

    //此处声明邮箱业务  --下方调用需要
    @Autowired
    private IMailservice mailservice;  //邮箱的业务

    /**
     * 获取邮箱注册账号的验证码
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/mail/register/vc
     * @param email  邮箱号
     * @return  响应数据查询验证码的关键词
     */


    @GetMapping("/register/vc")
    public ResponseData loginByEmailAndPassword(String email) {

        //验证邮箱参数是否为空
        if(Validator.isEmpty(email)){
            return new ResponseData(false,"邮箱参数有误", EventCode.ACCOUNT_EMAIL_WROME);
        }


        //调用邮箱注册账号的验证码业务
        try {
            String registerVC = mailservice.getEmailRegisterVC(email);
            return new ResponseData(true,"发送成功", EventCode.EMAIL_SEND_VC_SUCCESS,registerVC);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(),e.getCode());
        }

    }

}
