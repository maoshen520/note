package com.yun.note.util.validate;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.yun.note.exception.ValidateParamException;
import com.yun.note.pojo.User;
import com.yun.note.util.code.EventCode;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * token 验证的工具类
 */

public class TokenValidateUtil {

    public static User validateUserToken(String userToken, StringRedisTemplate redisTemplate) throws ValidateParamException {
        //验证userToken参数是否为空
        if(Validator.isEmpty(userToken)){
            throw new ValidateParamException("登录状态有误", EventCode.PARAM_USER_TOKEN_WRONG);
        }

        String userTokenRedisValue = null;
        try {
            //从redis中获取登录用户的信息
            userTokenRedisValue = redisTemplate.opsForValue().get(userToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidateParamException("服务错误", EventCode.REDIS_SERVE_ERROR);
        }

        //判断登录是否失效
        if(Validator.isEmpty(userTokenRedisValue)){
            throw new ValidateParamException("登录失效", EventCode.LOGIN_INVALID);
        }

        // 登录用户的对象
        return JSONUtil.toBean(userTokenRedisValue, User.class);
    }

}
