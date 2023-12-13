package com.zhike.note.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Thing;
import com.zhike.note.pojo.User;
import com.zhike.note.service.IThingService;
import com.zhike.note.util.code.EventCode;
import com.zhike.note.util.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 关于小记的控制层
 */


@RestController
@RequestMapping("/thing")  //请求地址:http://127.0.0.1:18081/zhike-notes/thing
public class ThingController {

    //先去util的ResponseData设置响应数据

    //此处声明用户的业务  --下方调用邮箱密码登录业务需要
    @Autowired
    private IThingService thingService;  //小记的业务

    @Autowired
    private StringRedisTemplate redisTemplate; // redis对象


    /**
     * 获取用户的小记列表
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/thing/list
     * @param userToken  邮箱号

     * @return  响应数据{user, userToken}
     */
    @GetMapping("/list")
    public ResponseData getUserThingList(@RequestHeader String userToken) {

        //验证userToken参数是否为空
        if(Validator.isEmpty(userToken)){
            return new ResponseData(false,"登录状态有误", EventCode.PARAM_USER_TOKEN_WRONG);
        }


        String userTokenRedisValue = null;
        try {
            //从redis中获取登录用户的信息
            userTokenRedisValue = redisTemplate.opsForValue().get(userToken);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(false,"小记服务错误", EventCode.REDIS_SERVE_ERROR);
        }

        //判断登录是否失效
        if(Validator.isEmpty(userTokenRedisValue)){
            return new ResponseData(false,"登录失效", EventCode.LOGIN_INVALID);
        }

        // 登录用户的对象
        User user = JSONUtil.toBean(userTokenRedisValue, User.class);

        try {
            //调用用户的小记列表业务
            List<Thing> things = thingService.getUserNormalThing(user.getId());
            return new ResponseData(true,"获取成功", EventCode.SELECT_SUCCESS, things);
        } catch (ServiceException e) {  //执行 List<Thing> things = thingService.getUserNormalThing(user.getId()); 业务报错，抛出ServiceException异常
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }

}
