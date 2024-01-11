package com.yun.note.service;

import com.yun.note.exception.ServiceException;

import java.util.Map;

//业务接口：用户的业务
public interface IUserservice {

    /*
    * 登录（邮箱号，密码）
    *
    * email  邮箱号
    * password  密码
    * return {user:登录成功后的用户信息，userToken:存到redis中的用户信息查询的关键词}
    *
    * */
    Map loginByEmailAndPassword(String email, String password) throws ServiceException;


    //获取邮箱是否已被注册
    void getEmailAccountIsExist(String email) throws ServiceException;

    /**
     *
     * 根据邮箱注册账号
     *
     * @param email
     * @throws ServiceException  业务异常
     */
    void registerAccountByEmail(String email) throws ServiceException;
}
