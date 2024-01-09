package com.zhike.note.controller;

import cn.hutool.core.lang.Validator;
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
import org.springframework.web.bind.annotation.*;

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


    /**
     * 置顶笔记（取消置顶）
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/note/top
     * @param isTop 是否置顶 笔记
     * @param noteId  笔记编号
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @GetMapping("/top")
    public ResponseData topNote(boolean isTop, int noteId, @RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            // 验证置顶参数
            if (Validator.isEmpty(isTop)) return new ResponseData(false, "置顶参数有误", EventCode.PARAM_NOTE_TOP_WRONG);

            //验证笔记编号参数
            if (Validator.isEmpty(noteId)) return new ResponseData(false, "笔记编号参数有误", EventCode.PARAM_NOTE_ID_WRONG);

            //调用置顶笔记业务
            noteService.topNote(isTop, noteId, user.getId());
            return new ResponseData(true,isTop ? "置顶成功" : "取消置顶成功", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {  //执行 List<Thing> things = thingService.getUserNormalThing(user.getId()); 业务报错，抛出ServiceException异常
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }


    /**
     * 删除笔记（彻底）
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/note/delete
     * @param complete 是否置顶 笔记
     * @param noteId  笔记编号
     * @param isRecycleBin  是否为回收站操作
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @DeleteMapping("/delete")
    public ResponseData deleteNote(boolean complete, int noteId, boolean isRecycleBin ,@RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            // 验证彻底参数
            if (Validator.isEmpty(complete)) return new ResponseData(false, "删除参数有误", EventCode.PARAM_NOTE_COMPLETE_WRONG);

            // 验回收站参数
            if (Validator.isEmpty(isRecycleBin)) return new ResponseData(false, "删除参数有误", EventCode.PARAM_NOTE_RECYCLE_BIN_WRONG);

            //验证小记编号参数
            if (Validator.isEmpty(noteId)) return new ResponseData(false, "笔记编号参数有误", EventCode.PARAM_NOTE_ID_WRONG);

            //调用删除笔记业务
            noteService.deleteNoteById(complete, noteId, user.getId(),isRecycleBin);
            return new ResponseData(true,complete ? "彻底删除成功" : "删除成功", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }

    /**
     * 新增笔记
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/note/create
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @PutMapping("/create")
    public ResponseData createNote(@RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            //调用新增笔记业务
            int noteId = noteService.createNoteInit(user.getId());
            return new ResponseData(true,"创建成功", EventCode.NOTE_CREATE_SUCCESS, noteId);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }

    /**
     * 获取编辑笔记信息
     * 请求地址 url:http://127.0.0.1:18081/zhike-notes/note/edit
     * @param noteId  小记编号
     * @param userToken  redis key 登录用户的信息
     * @return  响应数据
     */
    @GetMapping("/edit")
    public ResponseData getUserEditThing(int noteId, @RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            //验证编辑编号参数
            if (Validator.isEmpty(noteId)) return new ResponseData(false, "编号参数有误", EventCode.PARAM_THING_ID_WRONG);

            //调用置顶小记业务
            Note editNote = noteService.getEditNote(noteId, user.getId());
            return new ResponseData(true,"获取成功", EventCode.SELECT_SUCCESS, editNote);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }

}
