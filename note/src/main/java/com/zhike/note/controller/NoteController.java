package com.zhike.note.controller;

import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Note;
import com.zhike.note.pojo.Thing;
import com.zhike.note.pojo.User;
import com.zhike.note.service.INoteService;
import com.zhike.note.service.IThingService;
import com.zhike.note.util.code.EventCode;
import com.zhike.note.util.response.ResponseData;
import com.zhike.note.util.validate.TokenValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 笔记的控制层
 */
@RestController
@RequestMapping("/note") //请求地址:http://127.0.0.1:18081/note
public class NoteController {


    //此处声明用业务
    @Autowired
    private INoteService noteService;  //笔记的业务

    @Autowired
    private StringRedisTemplate redisTemplate; // redis对象


    /**
     * 获取用户的笔记列表
     * @param userToken
     * @return
     */

    @GetMapping("/list")
    public ResponseData getUserNoteList(@RequestHeader String userToken){

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            //调用用户的笔记列表业务
            List<Note> notes = noteService.getUserNormalNotes(user.getId());
            return new ResponseData(true,"获取成功", EventCode.SELECT_SUCCESS, notes);
        } catch (ServiceException e) {  //执行 List<Thing> things = thingService.getUserNormalThing(user.getId()); 业务报错，抛出ServiceException异常
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }




}
