package com.yun.note.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.yun.note.exception.ValidateParamException;
import com.yun.note.pojo.User;
import com.yun.note.service.IUserTokenService;
import com.yun.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserTokenServiceImpl implements IUserTokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 验证userToken
     * @param token
     * @return  用户的编号
     * @throws ValidateParamException
     */
    @Override
    public int validDator(String token) throws ValidateParamException {

        //验证userToken参数是否为空
        if(Validator.isEmpty(token)){
            throw new ValidateParamException("登录状态有误", EventCode.PARAM_USER_TOKEN_WRONG);
        }

        String userTokenRedisValue = null;
        try {
            //从redis中获取登录用户的信息
            userTokenRedisValue = redisTemplate.opsForValue().get(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidateParamException("服务错误", EventCode.REDIS_SERVE_ERROR);
        }

        //判断登录是否失效
        if(Validator.isEmpty(userTokenRedisValue)){
            throw new ValidateParamException("登录失效", EventCode.LOGIN_INVALID);
        }

        // 登录用户的编号
        return JSONUtil.toBean(userTokenRedisValue, User.class).getId();

    }
}
