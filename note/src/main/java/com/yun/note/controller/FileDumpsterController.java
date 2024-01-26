package com.yun.note.controller;

import cn.hutool.core.lang.Validator;
import com.yun.note.annotation.UserToken;
import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.FileDumpster;
import com.yun.note.pojo.Note;
import com.yun.note.pojo.User;
import com.yun.note.service.IFileDumpsterService;
import com.yun.note.service.INoteService;
import com.yun.note.util.code.EventCode;
import com.yun.note.util.response.ResponseData;
import com.yun.note.util.validate.TokenValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 回收站的控制层
 */
@Validated
@RestController
@RequestMapping("/file-dumpster") //请求地址:http://127.0.0.1:18081/note
public class FileDumpsterController {


    //此处声明用业务
    @Autowired
    private IFileDumpsterService fileDumpsterService;  //回收站的业务

    @Autowired
    private StringRedisTemplate redisTemplate; // redis对象


    /**
     * 获取用户的笔记列表
     * @param
     * @return
     */
    @UserToken
    @GetMapping("/list")
    public ResponseData getUserFiles(HttpServletRequest request){
        //从请求作用域中获取用户编号
        Integer userTokenID =(Integer) request.getAttribute("userTokenID");

        try {
            // 判断登录参数
            // User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            //调用用户的笔记列表业务
            List<FileDumpster> files = fileDumpsterService.getUserFiles(userTokenID);
            return new ResponseData(true,"获取成功", EventCode.SELECT_SUCCESS, files);
        } catch (ServiceException e) {  //执行 List<Thing> things = thingService.getUserNormalThing(user.getId()); 业务报错，抛出ServiceException异常
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }

//    public List<FileDumpster> getFiles (HttpServletRequest request){
//        //从请求作用域中获取用户编号
//        Integer userTokenID =(Integer) request.getAttribute("userTokenID");
//
//        //调用用户的回收站列表业务
//       return fileDumpsterService.getUserFiles(userTokenID);
//
//    }



}
