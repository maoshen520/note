package com.yun.note.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.yun.note.exception.ServiceRollbackException;
import com.yun.note.dao.IUserDao;
import com.yun.note.dao.IUserLogDao;
import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.User;
import com.yun.note.pojo.UserLog;
import com.yun.note.service.IUserservice;
import com.yun.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yun.note.pojo.table.Tables.USER;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
* 用户业务的实现层
*
* */
@Component
@Service
@Transactional(rollbackFor = {ServiceRollbackException.class})   //只要下面的方法抛出错误，回滚事务
public class UserServiceImpl implements IUserservice {

    @Autowired
    private IUserDao userDao;  //用户的数据库接口
    @Autowired
    private IUserLogDao userLogDao; // 用户日志的数据库接口
    @Autowired
    private StringRedisTemplate redisTemplate;  // redis对象


    /*
     * 登录（邮箱号，密码）
     *
     * email  邮箱号
     * password  密码
     * return {user:登录成功后的用户信息，userToken:存到redis中的用户信息查询的关键词}
     *  throws ServiceException  增加抛出的异常
     * */
    @Override
    public Map loginByEmailAndPassword(String email, String password) throws ServiceException {

        System.out.println(email);
        System.out.println(password);

        //根据邮箱和密码查询用户记录
        QueryWrapper wrapper = QueryWrapper.create()
                .select(USER.ID, USER.EMAIL, USER.NICKNAME, USER.HEAD_PIC, USER.LEVEL, USER.STATUS, USER.TIME)  //查询哪些字段
                .where(USER.EMAIL.eq(email))
                .and(USER.PASSWORD.eq(password));


        //通过数据库接口
        User user = null;
        try {
            user = userDao.selectOneByQuery(wrapper);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();

            //执行查询报错
            throw new ServiceException("登录服务错误", EventCode.SELECT_EXCEPTION);//抛出异常
        }

        if(user == null){
            //账号或密码报错
            throw new ServiceException("账号或密码错误", EventCode.SELECT_NONE);//抛出异常
        }

        if(user.getStatus() == 0){
            throw new ServiceException("账号被锁定", EventCode.ACCOUNT_CLOCK);//抛出异常
        }

        //新增用户日志（登录）
        UserLog log = UserLog.builder()
                .event(EventCode.LOGIN_EMAIL_PASSWORD_SUCCESS)
                .desc("邮箱密码登录")
                .time(new Date())
                .userId(user.getId())
                .build();
        System.out.println(log);

        int count = 0;
        try {
            count = userLogDao.insert(log);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("登录服务错误", EventCode.LOGIN_LOG_CREATE_EXCEPTION);//抛出异常
        }
        System.out.println(count);
        if(count != 1){
            throw new ServiceException("登录服务错误", EventCode.LOGIN_LOG_CREATE_FAIL);//抛出异常
        }

        //将登录的信息存储在redis中,14天，并将查询登录用户的关键词返回给客户端
        //生成唯一的 key 值   通过hutool工具
        String userTokenKey ="userToken" + IdUtil.randomUUID();

        try {
            redisTemplate.opsForValue().set(
                    userTokenKey,
                    JSONUtil.toJsonStr(user),
                    14,
                    TimeUnit.DAYS
            );
        } catch (Exception e) {
            e.printStackTrace();

            throw new ServiceException("登录服务错误", EventCode.LOGIN_SAVE_USER_TOKEN_REDIS_EXCEPTION);//抛出异常
        }

        //声明map对象,将登录用户信息和查询redis用户信息的关键词返回出去
        Map<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userToken",userTokenKey);

        return map;
    }


    /**
     * 获取邮箱是否已被注册
     *
     * @param email
     *
     */
    @Override
    public void getEmailAccountIsExist(String email) throws ServiceException {
        QueryWrapper wrapper = QueryWrapper.create()
                .where(USER.EMAIL.eq(email));

        long count = 0;
        try {
            count = userDao.selectCountByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("查询服务异常", EventCode.SELECT_EXCEPTION);
        }

        if (count != 0){
            throw new ServiceException("该邮箱账号已被注册", EventCode.ACCOUNT_EMAIL_REGISTERED);
        }
    }



    /**
     * 根据邮箱注册账号
     *
     * @param email
     * @throws ServiceException 业务异常
     */
    @Override
    public void registerAccountByEmail(String email) throws ServiceException {
        //获取邮箱是否已被注册
        getEmailAccountIsExist(email);

        Date localTime = new Date();

        // String password = RandomUtil.randomString(6);//初始密码
        String password = "123456";

        //新增用户的记录
        User user = User.builder()
                .email(email)
                .password(SecureUtil.md5(password))
                .time(localTime)
                .build();
        int count = 0;
        try {
            count = userDao.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("注册失败",EventCode.INSERT_EXCEPTION);
        }

        //只要错误，回滚 ServiceRollbackException
        if(count != 1){
            throw new ServiceRollbackException("注册失败",EventCode.INSERT_ERROR);
        }

        //新增一个用户日志（注册）
        UserLog log = UserLog.builder()
                .event(EventCode.ACCOUNT_EMAIL_REGISTER_SUCCESS)
                .desc("邮箱注册账号")
                .time(localTime)
                .userId(user.getId())
                .build();

        try {
            count = userLogDao.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceRollbackException("注册失败", EventCode.ACCOUNT_EMAIL_REGISTER_LOG_EXCEPTION);//抛出异常
        }
        if(count != 1){
            throw new ServiceRollbackException("注册失败", EventCode.ACCOUNT_EMAIL_REGISTER_LOG_ERROR);//抛出异常
        }

        //邮箱通知注册的用户，其初始密码
        String content = "<p>【笔记团队】尊敬的" + email + "：</p>" +
                "<p>您已成功注册笔记账号，其初始密码为：<b style='font-size:20px;color:blue;'>" + password + "</b>。</p>" +
                "<p>请尽量快速登录账号，修改其初始密码</p>";
        try {
            MailUtil.send(email,"笔记账号注册通知", content, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceRollbackException("注册失败",EventCode.EMAIL_SEND_INIT_PASSWORD_EXCEPTION);
        }

    }

}
