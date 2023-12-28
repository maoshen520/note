package com.zhike.note.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Thing;
import com.zhike.note.pojo.User;
import com.zhike.note.service.IThingService;
import com.zhike.note.util.code.EventCode;
import com.zhike.note.util.response.ResponseData;
import com.zhike.note.util.validate.TokenValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 关于小记的控制层
 */


@RestController
@RequestMapping("/thing")  //请求地址:http://127.0.0.1:18081/zhike-notes/thing
public class ThingController {

    //util的ResponseData设置响应数据

    //此处声明用业务
    @Autowired
    private IThingService thingService;  //小记的业务

    @Autowired
    private StringRedisTemplate redisTemplate; // redis对象


    /**
     * 获取用户的小记列表
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/thing/list
     * @param userToken

     * @return  响应数据{user, userToken}
     */
    @GetMapping("/list")
    public ResponseData getUserThingList(@RequestHeader String userToken) {

//        //验证userToken参数是否为空
//        if(Validator.isEmpty(userToken)){
//            return new ResponseData(false,"登录状态有误", EventCode.PARAM_USER_TOKEN_WRONG);
//        }
//
//
//        String userTokenRedisValue = null;
//        try {
//            //从redis中获取登录用户的信息
//            userTokenRedisValue = redisTemplate.opsForValue().get(userToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseData(false,"小记服务错误", EventCode.REDIS_SERVE_ERROR);
//        }
//
//        //判断登录是否失效
//        if(Validator.isEmpty(userTokenRedisValue)){
//            return new ResponseData(false,"登录失效", EventCode.LOGIN_INVALID);
//        }
//
//        // 登录用户的对象
//        User user = JSONUtil.toBean(userTokenRedisValue, User.class);

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            //调用用户的小记列表业务
            List<Thing> things = thingService.getUserNormalThing(user.getId());
            return new ResponseData(true,"获取成功", EventCode.SELECT_SUCCESS, things);
        } catch (ServiceException e) {  //执行 List<Thing> things = thingService.getUserNormalThing(user.getId()); 业务报错，抛出ServiceException异常
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }


    /**
     * 置顶小记（取消置顶）
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/thing/top
     * @param isTop 是否置顶 小记
     * @param thingId  小记编号
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @GetMapping("/top")
    public ResponseData topThing(boolean isTop, int thingId, @RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            // 验证置顶参数
            if (Validator.isEmpty(isTop)) return new ResponseData(false, "置顶参数有误", EventCode.PARAM_TOP_WRONG);

            //验证小记编号参数
            if (Validator.isEmpty(thingId)) return new ResponseData(false, "置顶参数有误", EventCode.PARAM_THING_ID_WRONG);

            //调用置顶小记业务
            thingService.topThing(isTop, thingId, user.getId());
            return new ResponseData(true,isTop ? "置顶成功" : "取消置顶成功", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {  //执行 List<Thing> things = thingService.getUserNormalThing(user.getId()); 业务报错，抛出ServiceException异常
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }


    /**
     * 删除小记（彻底）
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/thing/delete
     * @param complete 是否置顶 小记
     * @param thingId  小记编号
     * @param isRecycleBin  是否为回收站操作
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @DeleteMapping("/delete")
    public ResponseData deleteThing(boolean complete, int thingId, boolean isRecycleBin ,@RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            // 验证彻底参数
            if (Validator.isEmpty(complete)) return new ResponseData(false, "删除参数有误", EventCode.PARAM_THING_COMPLETE_WRONG);

            // 验回收站参数
            if (Validator.isEmpty(isRecycleBin)) return new ResponseData(false, "删除参数有误", EventCode.PARAM_THING_RECYCLE_BIN_WRONG);

            //验证小记编号参数
            if (Validator.isEmpty(thingId)) return new ResponseData(false, "小记编号参数有误", EventCode.PARAM_THING_ID_WRONG);

            //调用置顶小记业务
            thingService.deleteThingById(complete, thingId, user.getId(),isRecycleBin);
            return new ResponseData(true,complete ? "彻底删除成功" : "删除成功", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }


    /**
     * 新增小记
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/thing/create
     * @param title 标题
     * @param top  是否置顶
     * @param tags  标签
     * @param content  待办事项
     * @param finished  是否完成
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @PostMapping("/create")
    public ResponseData createThing(String title, boolean top, String tags, String content, boolean finished,@RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            // 验证小记标题参数
            if (Validator.isEmpty(title)) return new ResponseData(false, "小记标题参数有误", EventCode.PARAM_THING_TITLE_WRONG);

            // 验回小记置顶
            if (Validator.isEmpty(top)) return new ResponseData(false, "小记置顶参数有误", EventCode.PARAM_THING_TOP_WRONG);

            //验证小记标签参数
            if (Validator.isEmpty(tags)) return new ResponseData(false, "小记标签参数有误", EventCode.PARAM_THING_TAGS_WRONG);

            //验证小记待办事项参数
            if (Validator.isEmpty(content)) return new ResponseData(false, "小记待办事项参数有误", EventCode.PARAM_THING_CONTENT_WRONG);

            //验证小记完成参数
            if (Validator.isEmpty(finished)) return new ResponseData(false, "小记完成参数有误", EventCode.PARAM_THING_FINISHED_WRONG);

            //时间
            Date localTime = new Date();

            //创建对象
            Thing thing = Thing.builder()
                    .updateTime(localTime)
                    .time(localTime)
                    .title(title)
                    .tags(tags)
                    .content(content)
                    .userId(user.getId())
                    .finished(finished ? 1 : 0)
                    .top(top ? 1 : 0)
                    .build();

            //调用新增小记业务
            thingService.newCreateThing(thing);
            return new ResponseData(true,"新增成功", EventCode.THING_CREATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }
}
