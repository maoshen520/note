package com.yun.note.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.yun.note.exception.ServiceException;
import com.yun.note.exception.ServiceRollbackException;
import com.yun.note.service.IUserservice;
import com.yun.note.service.IMailservice;
import com.yun.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/*
* 邮箱业务的实现层
*
* */
@Component
@Service
@Transactional(rollbackFor = {ServiceRollbackException.class})   //只要下面的方法抛出错误，回滚事务
public class IMailServiceImpl implements IMailservice {

    @Autowired
    private IUserservice userservice;  //用户的业务

    @Autowired
    private StringRedisTemplate redisTemplate;  // redis对象


    /**
     *
     * 获取邮箱注册的验证码
     *
     * @param email 邮箱号
     * @return   查询验证码的关键词（redis）
     * @throws
     */
    @Override
    public String getEmailRegisterVC(String email) throws ServiceException {
        // 先判断邮箱是否已被注册
        //发送验证码到注册的邮箱中

        //先判断邮箱是否已被注册
        userservice.getEmailAccountIsExist(email);

        //发送验证码到注册的邮箱中
        String code = RandomUtil.randomString(6);  //随机生成6位字符（数值+字母）

        int time = 5;

        String content = "<p>【笔记团队】尊敬的" + email + "：</p>" +
                "<p>您正在申请注册账号服务，如本人操作，请勿泄露该验证码</p>" +
                "<p>验证码：<b style='font-size:20px;color:blue;'>" + code + "</b></p>" +
                "有效时间为 "+ time +" 分钟";


        try {
            MailUtil.send(email,"邮箱账号注册验证码", content, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("验证码发送失败", EventCode.EMAIL_SEND_VC_ERROR);
        }

        //将验证码保存在redis中5分钟
        String ereavcTokenKey = "eravcToken:" + email + ":" + IdUtil.randomUUID();

        try {
            redisTemplate.opsForValue().set(ereavcTokenKey, code, time, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("验证码存储失败", EventCode.EMAIL_SEND_VC_REDIS_ERROR);
        }

        return ereavcTokenKey;
    }
}
