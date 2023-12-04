package com.zhike.note.service;

import com.zhike.note.exception.ServiceException;

import java.util.Map;

//业务接口：邮箱的业务
public interface IMailservice {
    /*
    * 获取邮箱注册的验证码）
    *
    * email  邮箱号
    * return 查询验证码的关键词（redis）
    *
    * */
    String getEmailRegisterVC(String email) throws ServiceException;
}
